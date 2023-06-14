package hu.ponte.hr.controller;

import hu.ponte.hr.domain.ImageEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * @author zoltan
 */
@Getter
public class ImageMeta
{
	private String id;
	private String name;
	private String mimeType;
	private long size;
	private String digitalSign;


	public ImageMeta(ImageEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.mimeType = entity.getMimeType();
		this.size = entity.getSize();
		this.digitalSign = entity.getDigitalSign();
	}
}
