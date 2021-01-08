package com.moxi.mogublog.picture.form;

import com.moxi.mougblog.base.vo.FileVO;
import lombok.Data;

@Data
public class SearchPictureForm extends FileVO {
    private String searchKey;
    private Integer count;
}
