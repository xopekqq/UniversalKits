package ru.xopek.universalkits.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
@Entity
public class UserEntity {
    @Id
    public String userName;

    @Column @Getter @Setter
    public long kitStartTs;
    @Column @Getter @Setter
    private long kitDonateTs;

    @Column @Getter @Setter
    private int kitStartCount;

    @Column @Getter @Setter
    private int kitDonateCount;

    @Column @Getter
    private String userIp;

    public UserEntity() {}
    public UserEntity(String userName, String userAddress, long kitStartTs, long kitDonateTs, int kitStartCount, int kitDonateCount) {
        this.userName = userName;
        this.userIp = userAddress;
        this.kitStartTs = kitStartTs;
        this.kitDonateTs = kitDonateTs;
        this.kitStartCount = kitStartCount;
        this.kitDonateCount = kitDonateCount;
    }
}
