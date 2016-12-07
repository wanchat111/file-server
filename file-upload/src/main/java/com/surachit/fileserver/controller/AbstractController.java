package com.surachit.fileserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

import com.surachit.fileserver.response.Pagination;
import com.surachit.fileserver.response.Response;
import com.surachit.fileserver.response.Status;

public class AbstractController {
	/**
     * New success response.
     *
     * @param <T> the generic type
     * @return the response
     */
    private <T> Response<T> newSuccessResponse() {
        Status status = new Status();
        status.setCode("Success");

        Response<T> response = new Response<>();
        response.setStatus(status);

        return response;
    }

    /**
     * New error response.
     *
     * @param <T> the generic type
     * @param message the message
     * @return the response
     */
    private <T> Response<T> newErrorResponse(String message) {
        Status status = new Status();
        status.setCode("Error");
        status.setMessage(message);

        Response<T> response = new Response<>();
        response.setStatus(status);

        return response;
    }

    /**
     * Return with success.
     *
     * @return the response
     */
    public Response<Void> returnWithSuccess() {
        Response<Void> response = newSuccessResponse();
        return response;
    }

    /**
     * Return with success.
     *
     * @param <T> the generic type
     * @param data the data
     * @return the response
     */
    public <T> Response<T> returnWithSuccess(T data) {
        Response<T> response = newSuccessResponse();
        response.setData(data);
        return response;
    }

    /**
     * Return with success.
     *
     * @param <S> the generic type
     * @param <T> the generic type
     * @param entity the entity
     * @param transformer the transformer
     * @return the response
     */
    public <S, T> Response<T> returnWithSuccess(S entity, Function<S, T> transformer) {
        Response<T> response = newSuccessResponse();
        response.setData(transformer.apply(entity));
        return response;
    }

    /**
     * Return success with list.
     *
     * @param <S> the generic type
     * @param <T> the generic type
     * @param entities the entities
     * @param transformer the transformer
     * @return the response
     */
    public <S, T> Response<List<T>> returnSuccessWithList(List<S> entities,
            Function<S, T> transformer) {
        Response<List<T>> response = newSuccessResponse();

        List<T> targets = new ArrayList<T>();
        for (S source : entities) {
            T target = transformer.apply(source);
            targets.add(target);
        }
        response.setData(targets);
        return response;
    }

    /**
     * Return with error.
     *
     * @param <T> the generic type
     * @param message the message
     * @return the response
     */
    public <T> Response<T> returnWithError(String message) {
        Response<T> response = newErrorResponse(message);
        return response;
    }

    /**
     * Return success with pagination.
     *
     * @param <S> the generic type
     * @param <T> the generic type
     * @param entityPage the entity page
     * @param transformer the transformer
     * @param url the url
     * @return the response
     */
    public <S, T> Response<List<T>> returnSuccessWithPagination(Page<S> entityPage,
            Function<S, T> transformer, String url) {
        Response<List<T>> response = newSuccessResponse();

        List<T> targets = new ArrayList<T>();
        for (S source : entityPage.getContent()) {
            T target = transformer.apply(source);
            targets.add(target);
        }
        response.setData(targets);

        int page = entityPage.getNumber() + 1;
        int limit = entityPage.getSize();
        int total = entityPage.getTotalPages();
        Long grandTotal = entityPage.getTotalElements();

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setLimit(limit);
        pagination.setTotal(total);
        pagination.setGrandTotal(grandTotal);

        if (total != 1) {
            String next = "";
            String previous = "";
            if (page == 1 && page < total) {
                next = url + "?page=" + (page + 1) + "&limit=" + limit;
            } else if (page == total) {
                previous = url + "?page=" + (page - 1) + "&limit=" + limit;
            } else {
                next = url + "?page=" + (page + 1) + "&limit=" + limit;
                previous = url + "?page=" + (page - 1) + "&limit=" + limit;
            }

            pagination.setNext(next);
            pagination.setPrevious(previous);
        }

        response.setPagination(pagination);

        return response;
    }
}
