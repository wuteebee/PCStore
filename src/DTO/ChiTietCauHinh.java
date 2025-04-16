package DTO;

public abstract class ChiTietCauHinh {
        private String idSanpham;
        private String idThongTin;
        private int phienBan;
       
        public ChiTietCauHinh(){}
        public ChiTietCauHinh(String idSanPham,String idThongTin,int phienBan ){
          this.idSanpham=idSanPham;
          this.idThongTin=idThongTin;
          this.phienBan=phienBan;
        }

        public void setIdThongTin(String idThongTin) {
            this.idThongTin = idThongTin;
        }
        public void setPhienBan(int phienBan) {
            this.phienBan = phienBan;
        }
        public String getIdThongTin() {
            return idThongTin;
        }
        public int getPhienBan() {
            return phienBan;
        }
         

    
} 
