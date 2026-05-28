package com.saram.jellylog.furniture.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "furniture_table")
@Getter // 롬복이 자동으로 모든 Getter를 만들어줍니다.
@Setter // 롬복이 자동으로 모든 Setter를 만들어줍니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 자동 생성
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
    private Integer furniturePrice;

    @Column(name = "furniture_group")
    private Integer furnitureGroup;

    @Column(name = "furniture_position_x")
    private Integer furniturePositionX;

    @Column(name = "furniture_position_y")
    private Integer furniturePositionY;

    @Column(name = "furniture_depth_index")
    private Integer furnitureDepthIndex;

    // 커스텀 생성자
    public Furniture(String furnitureName, String furnitureImage, Integer furniturePrice, Integer furnitureGroup) {
        this.furnitureName = furnitureName;
        this.furnitureImage = furnitureImage;
        this.furniturePrice = furniturePrice;
        this.furnitureGroup = furnitureGroup;
    }

    public static Furniture create(String furnitureName, String furnitureImage, Integer furniturePrice, Integer furnitureGroup) {
        return new Furniture(furnitureName, furnitureImage, furniturePrice, furnitureGroup);
    }

    public void updateInfo(String furnitureName, String furnitureImage, Integer furniturePrice, Integer furnitureGroup) {
        this.furnitureName = furnitureName;
        this.furnitureImage = furnitureImage;
        this.furniturePrice = furniturePrice;
        this.furnitureGroup = furnitureGroup;
    }

    // 아래에 있던 모든 public getXXX, setXXX 메서드는 지우셔도 됩니다!
}