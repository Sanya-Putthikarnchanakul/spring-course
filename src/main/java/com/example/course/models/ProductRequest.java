package com.example.course.models;

// สำหรับ DTO สามารถใช้ record แทน class ได้
// ข้อแตกต่างระหว่าง class และ record คือ record จะไม่สามารถ set ค่าได้ เหมาะสำหรับสถานการณ์ที่ไม่ต้องการเปลี่ยนแปลงค่าของ class นั้น ๆ แล้ว
// ข้อดีของการใช้ record คือ จะมี boiler plate code น้อยกว่า class
public record ProductRequest(
        int id,
        String productName,
        Double price
) {
}
