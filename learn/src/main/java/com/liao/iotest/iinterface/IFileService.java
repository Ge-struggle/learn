package com.liao.iotest.iinterface;

import java.io.File;

public interface IFileService {

    public static final String SAVE_DIR="F:"+ File.separator+"liaozk"+File.separator;
    /**
     * 保存成功返回 ture
     * @return
     */
    public boolean save();
}
