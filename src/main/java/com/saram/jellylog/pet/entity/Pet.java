package com.saram.jellylog.domain.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet_table")
public class Pet {

    @Id
    @Column(name = "pet_code")
    private Long petCode;

    @Column(name = "pet_name")
    private String petName;

    @Column(name = "pet_default_image")
    private String petDefaultImage;

    @Column(name = "pet_active_image_1")
    private String petActiveImage1;

    @Column(name = "pet_active_image_2")
    private String petActiveImage2;

    @Column(name = "pet_active_image_3")
    private String petActiveImage3;

    @Column(name = "pet_active_image_4")
    private String petActiveImage4;

    @Column(name = "pet_active_image_5")
    private String petActiveImage5;

    @Column(name = "pet_active_image_6")
    private String petActiveImage6;

    @Column(name = "pet_active_image_7")
    private String petActiveImage7;

    @Column(name = "pet_active_image_8")
    private String petActiveImage8;

    public Pet() {
    }

    public Long getPetCode() {
        return petCode;
    }

    public void setPetCode(Long petCode) {
        this.petCode = petCode;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetDefaultImage() {
        return petDefaultImage;
    }

    public void setPetDefaultImage(String petDefaultImage) {
        this.petDefaultImage = petDefaultImage;
    }

    public String getPetActiveImage1() {
        return petActiveImage1;
    }

    public void setPetActiveImage1(String petActiveImage1) {
        this.petActiveImage1 = petActiveImage1;
    }

    public String getPetActiveImage2() {
        return petActiveImage2;
    }

    public void setPetActiveImage2(String petActiveImage2) {
        this.petActiveImage2 = petActiveImage2;
    }

    public String getPetActiveImage3() {
        return petActiveImage3;
    }

    public void setPetActiveImage3(String petActiveImage3) {
        this.petActiveImage3 = petActiveImage3;
    }

    public String getPetActiveImage4() {
        return petActiveImage4;
    }

    public void setPetActiveImage4(String petActiveImage4) {
        this.petActiveImage4 = petActiveImage4;
    }

    public String getPetActiveImage5() {
        return petActiveImage5;
    }

    public void setPetActiveImage5(String petActiveImage5) {
        this.petActiveImage5 = petActiveImage5;
    }

    public String getPetActiveImage6() {
        return petActiveImage6;
    }

    public void setPetActiveImage6(String petActiveImage6) {
        this.petActiveImage6 = petActiveImage6;
    }

    public String getPetActiveImage7() {
        return petActiveImage7;
    }

    public void setPetActiveImage7(String petActiveImage7) {
        this.petActiveImage7 = petActiveImage7;
    }

    public String getPetActiveImage8() {
        return petActiveImage8;
    }

    public void setPetActiveImage8(String petActiveImage8) {
        this.petActiveImage8 = petActiveImage8;
    }
}

