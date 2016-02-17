package contacts.pojo;

/**
 * Created by chaitanya on 15/02/16.
 */
public class ContactsData {

    String name;
    String phone;
    String photo;
    int id;

    public ContactsData(int id, String name, String phone, String photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        if (photo != null)
            this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
