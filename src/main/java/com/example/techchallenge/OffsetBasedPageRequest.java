// Extracted from https://blog.felix-seifert.com/limit-and-offset-spring-data-jpa-repositories/
package com.example.techchallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class OffsetBasedPageRequest implements Pageable {
    private int limit;
    private int offset;
    private String sort;

    private static final Logger log = LoggerFactory.getLogger(OffsetBasedPageRequest.class);

    public OffsetBasedPageRequest(int limit, int offset, String sort) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.limit = limit;
        this.offset = offset;
        if (sort == null) sort = "id";
        this.sort = sort;
    }
    @Override
    public int getPageNumber() {
        return offset / limit;
    }
    @Override
    public int getPageSize() {
        return limit;
    }
    @Override
    public long getOffset() {
        return offset;
    }
    @Override
    public Sort getSort() {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, sort.toLowerCase()).ignoreCase();
        return Sort.by(order);
    }
    @Override
    public Pageable next() {
        // Typecast possible because number of entries cannot be bigger than integer (primary key is integer)
        return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()), sort);
    }
    public Pageable previous() {
        // The integers are positive. Subtracting does not let them become bigger than integer.
        return hasPrevious() ?
                new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize()), sort): this;
    }
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }
    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasedPageRequest(getPageSize(), pageNumber*limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
