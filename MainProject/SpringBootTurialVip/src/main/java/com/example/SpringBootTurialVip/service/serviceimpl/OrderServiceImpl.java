package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.shopentity.Cart;
import com.example.SpringBootTurialVip.shopentity.OrderDetail;
import com.example.SpringBootTurialVip.shopentity.OrderRequest;
import com.example.SpringBootTurialVip.shopentity.ProductOrder;
import com.example.SpringBootTurialVip.shoprepository.CartRepository;
import com.example.SpringBootTurialVip.shoprepository.ProductOrderRepository;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.example.SpringBootTurialVip.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public void saveOrder(Long userid, OrderRequest orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid);

        for (Cart cart : carts) {

            ProductOrder order = new ProductOrder();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());

            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFirstName(orderRequest.getFirstName());
            orderDetail.setLastName(orderRequest.getLastName());
            orderDetail.setEmail(orderRequest.getEmail());
            orderDetail.setMobileNo(orderRequest.getMobileNo());
            orderDetail.setAddress(orderRequest.getAddress());




            ProductOrder saveOrder = orderRepository.save(order);
            commonUtil.sendMailForProductOrder(saveOrder, "success");
        }
    }

    @Override
    public List<ProductOrder> getOrdersByUser(Integer userId) {
        return List.of();
    }

    @Override
    public ProductOrder updateOrderStatus(Integer id, String status) {
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return List.of();
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return null;
    }

    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        return null;
    }
}
