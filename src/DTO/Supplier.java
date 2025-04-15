package DTO;

public class Supplier {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private boolean trangThai;

    public Supplier() {
        
    }

    public Supplier(String id, String name, String phoneNumber, String email, String address, boolean trangThai) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.trangThai = trangThai;
    }

    // Getter và Setter
    public String getId() { 
        return id; 
    }
    public void setId(String id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getPhoneNumber() { 
        return phoneNumber; 
    }

    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getAddress() { 
        return address; 
    }

    public void setAddress(String address) { 
        this.address = address; 
    }

    public boolean getTrangThai() { 
        return trangThai; 
    }

    public void setTrangThai(boolean trangThai) { 
        this.trangThai = trangThai; 
    }
}
