package com.ikea.productservice.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductServiceVO {
    private String name;
    private ProductVO product;
    @JsonProperty("contain_articles") List<ArticleVO> warehouseInventory;
    private List<ProductVO> products;
    private List<ArticleVO> articles;
    private List<String> artIds;
    private boolean operationStatus;
}
