package controller;

import dal.BoardingHouseDAO;
import dal.SubAreaDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import models.BoardingHouse;
import models.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024,
        fileSizeThreshold = 0
)
public class BoardingHouseServlet extends HttpServlet {

    private static final String UPLOAD_SUBDIR = "uploads/boarding";
    private static final long MAX_IMAGE_BYTES = 5 * 1024 * 1024;

    private final BoardingHouseDAO boardingHouseDAO = new BoardingHouseDAO();
    private final SubAreaDAO subAreaDAO = new SubAreaDAO();
    private final UserDAO userDAO = new UserDAO();

    private List<BoardingHouse> getListForUser(User user) {
        if (user == null) return List.of();
        if ("ADMIN".equals(user.getRole())) return boardingHouseDAO.getAll();
        return boardingHouseDAO.getByLandlordId(user.getId());
    }

    private static boolean allowedImageType(String contentType) {
        if (contentType == null) {
            return false;
        }
        String c = contentType.toLowerCase();
        return "image/jpeg".equals(c) || "image/png".equals(c) || "image/webp".equals(c);
    }

    private static String pickExtension(Part part) {
        String submitted = part.getSubmittedFileName();
        if (submitted != null) {
            int dot = submitted.lastIndexOf('.');
            if (dot >= 0) {
                String ext = submitted.substring(dot).toLowerCase();
                if (".jpg".equals(ext) || ".jpeg".equals(ext)) {
                    return ".jpg";
                }
                if (".png".equals(ext)) {
                    return ".png";
                }
                if (".webp".equals(ext)) {
                    return ".webp";
                }
            }
        }
        String ct = part.getContentType();
        if (ct != null) {
            if ("image/png".equalsIgnoreCase(ct)) {
                return ".png";
            }
            if ("image/webp".equalsIgnoreCase(ct)) {
                return ".webp";
            }
        }
        return ".jpg";
    }

    private String saveUploadedImage(Part part, jakarta.servlet.ServletContext ctx) throws IOException {
        String base = ctx.getRealPath("/" + UPLOAD_SUBDIR);
        if (base == null) {
            throw new IOException("Thư mục upload không khả dụng (getRealPath null). Hãy deploy dạng exploded WAR.");
        }
        File dir = new File(base);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Không tạo được thư mục upload.");
        }
        String name = UUID.randomUUID() + pickExtension(part);
        Path target = Path.of(dir.getAbsolutePath(), name);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return UPLOAD_SUBDIR + "/" + name;
    }

    private void deleteStoredImage(jakarta.servlet.ServletContext ctx, String relativePath) {
        if (relativePath == null || relativePath.isBlank()) {
            return;
        }
        String rel = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        String base = ctx.getRealPath("/" + rel);
        if (base != null) {
            File f = new File(base);
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    private void forwardFormError(HttpServletRequest request, HttpServletResponse response, User user,
            BoardingHouse boardinghouse, String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("boardinghouse", boardinghouse);
        request.setAttribute("subareas", subAreaDAO.getAll());
        if ("ADMIN".equals(user.getRole())) {
            request.setAttribute("users", userDAO.getAllUsers());
        }
        request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || ("STUDENT".equals(user.getRole()))) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                BoardingHouse b = boardingHouseDAO.getById(Integer.parseInt(idStr));
                if (b != null && ("ADMIN".equals(user.getRole()) || user.getId().equals(b.getLandlordId()))) {
                    request.setAttribute("boardinghouse", b);
                }
            }
            request.setAttribute("subareas", subAreaDAO.getAll());
            if ("ADMIN".equals(user.getRole())) request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
            return;
        }
        if ("add".equals(action)) {
            request.setAttribute("subareas", subAreaDAO.getAll());
            if ("ADMIN".equals(user.getRole())) request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/views/boardinghouse/form.jsp").forward(request, response);
            return;
        }
        request.setAttribute("boardinghouses", getListForUser(user));
        request.setAttribute("subareas", subAreaDAO.getAll());
        if ("ADMIN".equals(user.getRole())) {
            Map<Integer, String> landlordLabels = new HashMap<>();
            for (User u : userDAO.getAllUsers()) {
                if (u.getId() == null) {
                    continue;
                }
                String name = u.getFullName() != null ? u.getFullName().trim() : "";
                String email = u.getEmail() != null ? u.getEmail().trim() : "";
                String label;
                if (!name.isEmpty() && !email.isEmpty()) {
                    label = name + " (" + email + ")";
                } else if (!name.isEmpty()) {
                    label = name;
                } else if (!email.isEmpty()) {
                    label = email;
                } else {
                    label = "User #" + u.getId();
                }
                landlordLabels.put(u.getId(), label);
            }
            request.setAttribute("landlordLabels", landlordLabels);
        }
        request.getRequestDispatcher("/views/boardinghouse/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || "STUDENT".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    BoardingHouse b = boardingHouseDAO.getById(id);
                    if (b != null && ("ADMIN".equals(user.getRole()) || user.getId().equals(b.getLandlordId()))) {
                        deleteStoredImage(request.getServletContext(), b.getImagePath());
                        boardingHouseDAO.delete(id);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            response.sendRedirect(request.getContextPath() + "/boardinghouse");
            return;
        }
        String idStr = request.getParameter("id");
        BoardingHouse existing = null;
        if (idStr != null && !idStr.isEmpty()) {
            try {
                existing = boardingHouseDAO.getById(Integer.parseInt(idStr));
            } catch (NumberFormatException ignored) {
            }
        }

        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String subAreaIdStr = request.getParameter("subAreaId");
        if (name == null || name.trim().isEmpty() || subAreaIdStr == null || subAreaIdStr.isEmpty()) {
            forwardFormError(request, response, user, existing, "Vui lòng điền đầy đủ thông tin.");
            return;
        }
        int landlordId = user.getId();
        if ("ADMIN".equals(user.getRole())) {
            String lid = request.getParameter("landlordId");
            if (lid != null && !lid.isEmpty()) {
                try {
                    landlordId = Integer.parseInt(lid);
                } catch (NumberFormatException e) {
                    forwardFormError(request, response, user, existing, "Chủ trọ không hợp lệ.");
                    return;
                }
            }
        }
        int subAreaId;
        try {
            subAreaId = Integer.parseInt(subAreaIdStr);
        } catch (NumberFormatException e) {
            forwardFormError(request, response, user, existing, "Khu vực con không hợp lệ.");
            return;
        }

        Part imagePart = request.getPart("image");
        boolean removeImage = "1".equals(request.getParameter("removeImage"));

        String imagePathToSave;
        String newlySavedRelative = null;
        try {
            if (removeImage) {
                if (existing != null) {
                    deleteStoredImage(request.getServletContext(), existing.getImagePath());
                }
                imagePathToSave = null;
            } else if (imagePart != null && imagePart.getSize() > 0) {
                if (imagePart.getSize() > MAX_IMAGE_BYTES) {
                    forwardFormError(request, response, user, existing, "Ảnh tối đa 5 MB.");
                    return;
                }
                if (!allowedImageType(imagePart.getContentType())) {
                    forwardFormError(request, response, user, existing, "Chỉ chấp nhận ảnh JPEG, PNG hoặc WebP.");
                    return;
                }
                newlySavedRelative = saveUploadedImage(imagePart, request.getServletContext());
                if (existing != null && existing.getImagePath() != null && !existing.getImagePath().isBlank()) {
                    deleteStoredImage(request.getServletContext(), existing.getImagePath());
                }
                imagePathToSave = newlySavedRelative;
            } else {
                imagePathToSave = existing != null ? existing.getImagePath() : null;
            }
        } catch (IOException ex) {
            if (newlySavedRelative != null) {
                deleteStoredImage(request.getServletContext(), newlySavedRelative);
            }
            forwardFormError(request, response, user, existing, "Lỗi lưu ảnh: " + ex.getMessage());
            return;
        }

        if (idStr != null && !idStr.isEmpty()) {
            BoardingHouse b = existing;
            if (b != null && ("ADMIN".equals(user.getRole()) || user.getId().equals(b.getLandlordId()))) {
                b.setName(name.trim());
                b.setAddress(address != null ? address.trim() : "");
                b.setSubAreaId(subAreaId);
                b.setLandlordId(landlordId);
                b.setImagePath(imagePathToSave);
                boardingHouseDAO.update(b);
            }
        } else {
            BoardingHouse b = new BoardingHouse(null, landlordId, subAreaId, name.trim(),
                    address != null ? address.trim() : "", imagePathToSave);
            boardingHouseDAO.insert(b);
        }
        response.sendRedirect(request.getContextPath() + "/boardinghouse");
    }
}
