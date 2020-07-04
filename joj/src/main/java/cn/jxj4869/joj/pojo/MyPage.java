package cn.jxj4869.joj.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MyPage<T> extends Page<T> {
    private Long begin;
    private Long end;
    private Integer showPages = 10;

    public MyPage() {
    }

    public MyPage(long current, long size) {
        super(current, size);
    }

    public MyPage(long current, long size, long total) {
        super(current, size, total);
    }

    public MyPage(long current, long size, boolean isSearchCount) {
        super(current, size, isSearchCount);
    }

    public MyPage(long current, long size, long total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);
    }


    public void setShowBtnNum() {
        if (this.getPages() < this.showPages) {
            this.begin = 1l;
            this.end = Math.max(this.getPages(),1);

        } else {
            this.begin = this.getCurrent() - (this.showPages / 2);
            this.end = this.getCurrent() + (this.showPages % 2 == 0 ? this.showPages / 2 - 1 : this.showPages / 2);

            if (this.begin < 1) {
                this.begin = 1l;
                this.end = this.begin + this.showPages - 1;
            }

            if (this.end > this.getPages()) {
                this.end = this.getCurrent();
                this.begin = this.end - (this.showPages - 1);
            }

        }
    }
}
