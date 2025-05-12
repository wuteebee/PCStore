package GUI.Components;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.File;
public class Excel {
    
    public String ChooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new File("DanhSach.xlsx"));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            System.out.println("Lưu file tại: " + filePath);
            return filePath;
            // Gọi phương thức xuất Excel ở đây
      
        }
        else {
            System.out.println("Người dùng đã hủy chọn.");
            return null;
        }
    }
}

