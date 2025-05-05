package tn.esprit.interfaces;

import java.util.List;
public interface Icrud<T> {


    boolean add(T t);
    List<T> getAll();
    boolean update (T t);
    boolean delete (T t);

}
