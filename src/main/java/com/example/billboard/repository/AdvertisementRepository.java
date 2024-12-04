package com.example.billboard.repository;

import com.example.billboard.model.Advertisement;
import com.example.billboard.model.Device;
import com.example.billboard.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    Optional<Advertisement> findById(Long aLong);

    List<Advertisement> findByHostDeviceId(Long ownerId);
    List<Advertisement> findByMediaFileId(MediaFile aLong);
//    void deleteAllByHostDevice(Device device);
    @Query(nativeQuery = true,
            value = """
            
            SELECT COUNT(*)
            FROM advertisements
            WHERE device_id = :deviceId
                
            """)
    Integer getAdvertisementCount(@Param("deviceId") Long deviceId);

    @Query(nativeQuery = true,
            value = """
            SELECT COUNT(*)
            FROM advertisements
            """)
    Integer getAllAdvertisementCount();

}
