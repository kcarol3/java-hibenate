package com.lg;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("JPa projects");
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("Hibernate_JPA");
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        User u1 = new User(null, "test_1","test_1","Andrzej",
                "Kowalski", Sex.MALE);
        User u2 = new User(null, "test_2","test_2","Ryszarda",
                "Kowalska", Sex.FEMALE);
        User u3 = new User(null, "test_3","test_3","Andrzej",
                "Kowalski", Sex.MALE);
        User u4 = new User(null, "test_4","test_4","Andrzej",
                "Kowalski", Sex.MALE);
        User u5 = new User(null, "test_5","test_5","Andrzej",
                "Kowalski", Sex.MALE);
        Role r1 = new Role(null, "role1");
        Role r2 = new Role(null, "role2");
        Role r3 = new Role(null, "role3");
        Role r4 = new Role(null, "role4");
        Role r5 = new Role(null, "role5");
        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);
        em.persist(u5);
        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
        em.persist(r4);
        em.persist(r5);

        //zmiana hasla dla uzytkownika o id 1
        User us = em.find(User.class, 1L);
        if (us != null) {
            us.setPassword("newPassword");
            em.merge(us);
        }

        //usuwanie roli o id = 5
        Role r = em.find(Role.class, 5L);
        if (r != null){
            em.remove(r);
        }

        //wyswietlanie Kowalskich
        Query query = em.createQuery("SELECT u FROM User u WHERE u.lastName = 'Kowalski'");
        List<User> kowalscy = query.getResultList();
        System.out.println("Id ludzi o nazwisku Kowalski");
        for (User u: kowalscy){
            System.out.println(u.getId());
        }

        //Wyswietlanie kobiet
        Query w = em.createQuery("SELECT k FROM User k WHERE k.sex='FEMALE'");
        List<User> womens = w.getResultList();
        System.out.println("Id wszystkich kobiet");
        for (User i: womens){
            System.out.println(i.getId());
        }


        User u6 = new User(null, "test_6","test_6","Marcin",
                "Wiśniewski", Sex.MALE);
        User u7 = new User(null, "test_7","test_7","Andrzej",
                "Kozera", Sex.MALE);

        u6.addRole(r1);
        u6.addRole(r2);
        u7.addRole(r3);
        u7.addRole(r4);

        // dodanie userów do grup
        UserGroup group1 = new UserGroup();
        em.persist(group1);
        u5.addGroup(group1);
        u6.addGroup(group1);
        u7.addGroup(group1);
        em.persist(group1);
        UserGroup group2 = new UserGroup();
        em.persist(group2);
        u5.addGroup(group2);
        u4.addGroup(group2);

        em.persist(u6);

        //dodanie obrazka
        File file = new File("C:/Users/Karol/Pictures/krajobraz.jpg");
        BufferedImage image = ImageIO.read(file);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        u7.setImage(bytes);

        em.persist(u7);


        em.getTransaction().commit();
        em.close();
        factory.close();
    }
}
