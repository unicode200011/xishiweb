package com.stylefeng.guns.core.page;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * 分页结果的封装(for Bootstrap Table)
 *
 * @author fengshuonan
 * @Date 2017年1月22日 下午11:06:41
 */
public class PageInfoBT<T> {

    // 结果集
    private List<T> records;

    // 总数
    private long total;

    public PageInfoBT(Page<T> page) {
        this.records = page.getRecords();
        this.total = page.getTotal();
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> rows) {
        this.records = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
