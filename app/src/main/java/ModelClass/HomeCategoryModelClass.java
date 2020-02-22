package ModelClass;

/**
 * Created by wolfsoft5 on 8/1/19.
 */

public class HomeCategoryModelClass {

    Integer image,imagebtn;


    public HomeCategoryModelClass(Integer image, Integer imagebtn) {
        this.image = image;
        this.imagebtn = imagebtn;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getImagebtn() {
        return imagebtn;
    }
}
