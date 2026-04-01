package e_commerce.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private int currentPage;//trang hiện tại
    private int totalPages;//tổng số trang
    private int pageSize;//số lượng phần tử trong trang
    private long totalElements;//tổng số phần tử
    private List<T> data;
}