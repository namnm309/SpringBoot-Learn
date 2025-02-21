package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.shopentity.Product;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    //Thêm sản phẩm
    public Product addProduct(Product product);

    //Lấy danh sách sản phẩm
    public List<Product> getAllProducts();

    //Xóa sản phẩm = id product
    public Boolean deleteProduct(Integer id);

    //Kiếm sản phẩm bằng id,dùng để update product
    public Product getProductById(Integer id);

    //Kiếm sản phẩm = tên
    public List<Product> getProductByTitle(String title);

    //Cập nhật sản phẩm = id , cả hình
    public Product updateProduct(Product product, MultipartFile file);

    //Search profuct by tên
    public List<Product> searchProduct(String name);


}
