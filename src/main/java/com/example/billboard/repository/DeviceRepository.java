package com.example.billboard.repository;

import com.example.billboard.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findById(Long aLong);
    List<Device> findByOwnerId(Long ownerId);

    List<Device> findByDeviceGroupIDIsNullAndOwnerId(Long ownerId);
    List<Device> findAllByDeviceGroupIDIsNull();
    List<Device> findByDeviceGroupID(Long deviceGroup);

    @Query(nativeQuery = true,
            value = """
            SELECT COUNT(*)
            FROM devices
                
            """)
    Integer getAllDeviceCount();

    @Query(nativeQuery = true,
            value = """
            SELECT COUNT(*)
            FROM devices
            WHERE user_id = :ownerId
            """)
    Integer getDeviceCountByOwnerId(@Param("ownerId") Long ownerId);
//    void deleteDevicesById(List<Long> selectedDeviceIds);

    @Query("SELECT MIN(d.deviceGroupID) FROM Device d WHERE d.deviceGroupID IS NOT NULL")
    Long getMinUsedGroupNumber();

    @Query("SELECT COUNT(d) FROM Device d WHERE d.deviceGroupID IS NOT NULL")
    Long countDevicesWithDeviceGroupIDNotNull();

    @Query("SELECT COALESCE(MAX(d.deviceGroupID), 0) + 1 FROM Device d")
    Long getNextGroupNumber();
}
