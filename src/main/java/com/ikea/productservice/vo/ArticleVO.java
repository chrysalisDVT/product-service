package com.ikea.productservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(value = Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleVO {
	
	public ArticleVO(String artId,String amountOf){
		this.artId=artId;
		this.amountOf=amountOf;
	}
	@JsonProperty("art_id")
	String artId;
	@JsonProperty("amount_of")
	String amountOf;
	private String name;
	private String stock;
}
