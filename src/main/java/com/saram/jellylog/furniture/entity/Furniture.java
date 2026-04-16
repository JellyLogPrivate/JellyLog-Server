package com.saram.jellylog.furniture.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "furniture_table")
public class Furniture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furniture_code")
    private Long furnitureCode;

    @Column(name = "furniture_name")
    private String furnitureName;

    @Column(name = "furniture_image")
    private String furnitureImage;

    @Column(name = "furniture_price")
    private Long furniturePrice;

    @Column(name = "furniture_group")
    private String furnitureGroup;

    @Column(name = "furniture_position_x")
    private Integer furniturePositionX;

    @Column(name = "furniture_position_y")
    private Integer furniturePositionY;

    @Column(name = "furniture_depth_index")
    private Integer furnitureDepthIndex;

    public Furniture() {
    }

    public Long getFurnitureCode() {
        return furnitureCode;
    }

    public void setFurnitureCode(Long furnitureCode) {
        this.furnitureCode = furnitureCode;
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public void setFurnitureName(String furnitureName) {
        this.furnitureName = furnitureName;
    }

    public String getFurnitureImage() {
        return furnitureImage;
    }

    public void setFurnitureImage(String furnitureImage) {
        this.furnitureImage = furnitureImage;
    }

    public Long getFurniturePrice() {
        return furniturePrice;
    }

    public void setFurniturePrice(Long furniturePrice) {
        this.furniturePrice = furniturePrice;
    }

    public String getFurnitureGroup() {
        return furnitureGroup;
    }

    public void setFurnitureGroup(String furnitureGroup) {
        this.furnitureGroup = furnitureGroup;
    }

    public Integer getFurniturePositionX() {
        return furniturePositionX;
    }

    public void setFurniturePositionX(Integer furniturePositionX) {
        this.furniturePositionX = furniturePositionX;
    }

    public Integer getFurniturePositionY() {
        return furniturePositionY;
    }

    public void setFurniturePositionY(Integer furniturePositionY) {
        this.furniturePositionY = furniturePositionY;
    }

    public Integer getFurnitureDepthIndex() {
        return furnitureDepthIndex;
    }

    public void setFurnitureDepthIndex(Integer furnitureDepthIndex) {
        this.furnitureDepthIndex = furnitureDepthIndex;
    }
}
