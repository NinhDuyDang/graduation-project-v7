
package com.book_store.service;
import com.book_store.entity.Notify;
import com.book_store.repository.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
@Service
public class NotifyService {
    @Autowired
    private NotifyRepository notifyRepo;

    public void save(Notify noti) {
        notifyRepo.save(noti);
    }
    public List<Notify> findAll() {
        List<Notify> noti = notifyRepo.findAllByCustomerIdIsNull();
        noti.sort(Comparator.comparing(Notify::getCreatedAt).reversed());
        return noti;
    }
    public List<Notify> findByCustomer(int id) {
        List<Notify> noti = notifyRepo.findByCustomerId(id);
        noti.sort(Comparator.comparing(Notify::getCreatedAt).reversed());
        return noti;
    }
}
