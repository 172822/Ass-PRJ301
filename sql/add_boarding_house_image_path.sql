USE [PRJ301];
GO

IF NOT EXISTS (
    SELECT 1 FROM sys.columns
    WHERE object_id = OBJECT_ID(N'dbo.boarding_house') AND name = N'image_path'
)
BEGIN
    ALTER TABLE dbo.boarding_house
    ADD image_path NVARCHAR(500) NULL;
END
GO
