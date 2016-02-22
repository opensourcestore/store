package opensource.onlinestore.service;

import opensource.onlinestore.model.dto.GoodsDTO;

import java.io.File;
import java.util.List;

/**
 * Created by orbot on 09.02.16.
 */
public interface FileGoodsParser {

    List<GoodsDTO> parseGoodsFromFiles(List<File> files);
}
