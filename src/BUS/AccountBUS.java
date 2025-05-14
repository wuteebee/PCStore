package BUS;

import DAO.AccountDAO;
import DTO.Account;
import java.util.Comparator;
import java.util.List;

public class AccountBUS {
    private final AccountDAO accountDAO;
    private final PermissionBUS permissionBUS;

    public AccountBUS() {
        accountDAO = new AccountDAO();
        permissionBUS = new PermissionBUS();
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean saveAccount(String idTaiKhoan, String idNhanVien, byte[] anhDaiDien, String idNhomQuyen, 
                              String tenDangNhap, String matKhau, String maOTP, boolean isEdit) {
        try {
            Account account = new Account();
            account.setIdTaiKhoan(idTaiKhoan);
            account.setIdNhanVien(idNhanVien);
            account.setAnhDaiDien(anhDaiDien);
            account.setIdNhomQuyen(idNhomQuyen);
            account.setTenDangNhap(tenDangNhap);
            account.setMatKhau(matKhau);
            account.setTrangThai(1);
            account.setMaOTP(maOTP);

            return isEdit ? accountDAO.updateAccount(account) : accountDAO.insertAccount(account);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAccount(String idTaiKhoan) {
        return accountDAO.deleteAccount(idTaiKhoan);
    }

    public List<String> getAvailableEmployees() {
        return accountDAO.getAvailableEmployees();
    }

    public List<String> getPermissionGroups() {
        return accountDAO.getPermissionGroups();
    }

    public String generateAccountId() {
        List<Account> accounts = accountDAO.getAllAccounts();
        accounts.sort(Comparator.comparing(Account::getIdTaiKhoan));
        String lastId = accounts.isEmpty() ? null : accounts.get(accounts.size() - 1).getIdTaiKhoan();
        if (lastId == null) {
            return "TK001";
        }
        try {
            int number = Integer.parseInt(lastId.replace("TK", ""));
            return String.format("TK%03d", number + 1);
        } catch (NumberFormatException e) {
            return "TK001";
        }
    }

    public String getEmployeeName(String idNhanVien) {
        return accountDAO.getEmployeeName(idNhanVien);
    }

    public String getPermissionGroupName(String idNhomQuyen) {
        return accountDAO.getPermissionGroupName(idNhomQuyen);
    }

    public boolean hasPermission(String idTaiKhoan, String chucNang, String hanhDong) {
        Account account = accountDAO.getAllAccounts().stream()
                .filter(a -> a.getIdTaiKhoan().equals(idTaiKhoan))
                .findFirst()
                .orElse(null);
        if (account == null) {
            return false;
        }
        String idChucNang = permissionBUS.getChucNangIdByName(chucNang);
        return permissionBUS.hasPermission(account.getIdNhomQuyen(), idChucNang, hanhDong);
    }
}