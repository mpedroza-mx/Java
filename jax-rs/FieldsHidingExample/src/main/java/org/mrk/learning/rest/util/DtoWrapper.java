package org.mrk.learning.rest.util;

public class DtoWrapper<T> {

    private T entity;
    private Class clazzForFiltering;

    private DtoWrapper(){

    }

    public T getEntity() {
        return entity;
    }

    public Class getClazzForFiltering() {
        return clazzForFiltering;
    }

    public static class Builder<T>{
        private T entity;
        private Class clazzForFiltering;

        public Builder(T entity) {
            this.entity = entity;

        }

        public Builder<T> setClazzForFiltering(Class clazzForFiltering) {
            this.clazzForFiltering = clazzForFiltering;
            return this;
        }

        public DtoWrapper build(){
            DtoWrapper wrapper= new DtoWrapper();
            wrapper.entity = this.entity;
            wrapper.clazzForFiltering = this.clazzForFiltering;
            return  wrapper;
        }
    }




}
