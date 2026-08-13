# Quản Lý Khách Sạn

Ứng dụng Quản Lý Khách Sạn — đồ án môn học **Công nghệ phần mềm**.

## Giới thiệu

Hệ thống hỗ trợ nghiệp vụ quản lý khách sạn theo mô hình cơ cấu tổ chức thực tế, phục vụ nhiều bộ phận: Quản lý, Lễ tân & CSKH, Buồng phòng & Kỹ thuật, Kế toán & Tài chính, Nhân sự & Hành chính, Quản trị hệ thống. Ứng dụng bao gồm phần backend web quản lý dữ liệu tập trung và ứng dụng Android phục vụ thao tác nghiệp vụ hằng ngày.

## Cơ cấu tổ chức & nghiệp vụ

| Bộ phận | Mô tả |
|---|---|
| Quản lý | Phê duyệt và giám sát hoạt động khách sạn, theo dõi báo cáo, phê duyệt nghiệp vụ quan trọng |
| Lễ tân & CSKH | Tiếp nhận thông tin, xử lý đặt phòng, xử lý yêu cầu khách hàng, tiếp nhận yêu cầu dịch vụ |
| Buồng phòng & Kỹ thuật | Dọn dẹp phòng, kiểm kê tài sản phòng, bảo trì thiết bị |
| Kế toán & Tài chính | Quản lý doanh thu, hóa đơn, chi phí, báo cáo tài chính, đối soát dữ liệu đặt phòng/dịch vụ |
| Nhân sự & Hành chính | Quản lý hồ sơ nhân viên, tuyển dụng, xếp ca làm việc, lưu trữ hành chính |
| Quản trị hệ thống | Quản lý phần mềm, tài khoản, phân quyền truy cập và bảo mật hệ thống |

## Tính năng chính

- Xem danh sách phòng theo trạng thái
- Đặt phòng, hủy/sửa đặt phòng
- Xem lịch sử đặt phòng
- Quản lý khách hàng (tìm kiếm theo tên, SĐT, CCCD)
- Quản lý tài sản phòng, cập nhật thông tin phòng
- Quản lý dọn phòng
- Xem báo cáo doanh thu và biểu đồ sử dụng dịch vụ
- Ghi nhận yêu cầu dịch vụ từ khách, xem danh sách dịch vụ đã đặt
- Tính tổng tiền dịch vụ khách hàng đã dùng
- Xác nhận và xóa yêu cầu dịch vụ
- Quản lý ca làm việc nhân viên
- Thêm mới thông tin nhân viên
- Xuất danh sách khách lưu trú
- Thống kê phòng, khách, doanh thu
- Phân quyền và quản lý tài khoản người dùng

## Kiến trúc hệ thống

Ứng dụng được triển khai trong mạng LAN theo mô hình client - server:

- **Web Server (Apache):** xử lý nghiệp vụ theo mô hình MVC (Admin, Model, View, Controller, Uploads)
- **Cơ sở dữ liệu MySQL:** lưu trữ dữ liệu tập trung (`phuctong_db`)
- **Ứng dụng Android:** giao diện thao tác cho nhân viên/khách hàng, xây dựng theo mô hình Activity/Fragment, sử dụng RecyclerView để hiển thị danh sách (phòng, khách hàng, nhân viên, dịch vụ, thống kê)
- **Firebase:** hỗ trợ xác thực và một phần lưu trữ dữ liệu (kết hợp gọi API hoặc truy vấn trực tiếp)
- Giao tiếp: Client ↔ Web Server qua HTTP (port 80); Web Server ↔ Database Server qua kết nối MySQL

## Thiết kế dữ liệu

Các bảng dữ liệu chính:

- `KhachHang` — thông tin khách hàng (mã định danh, CCCD, họ tên...)
- `NhanVien` — thông tin nhân viên
- `QuanLy` — thông tin quản lý
- `Phong` — thông tin phòng, trạng thái phòng
- `DatPhong` — thông tin đặt phòng
- `Yeu_Cau_Dich_Vu` — yêu cầu dịch vụ của khách

## Tài liệu

Tài liệu phân tích và thiết kế chi tiết của đồ án — bao gồm Usecase Diagram, Activity Diagram, Sequence Diagram, Class Diagram, Statechart Diagram, Deployment Diagram, thiết kế cơ sở dữ liệu và thiết kế giao diện — được trình bày trong file `CNPM-BAOCAO-KHACHSAN.pdf` đính kèm.

## Nội dung báo cáo

1. **Lab 1 — Xác định yêu cầu:** mô hình tổ chức, nhu cầu người dùng, biểu mẫu, danh sách và mô tả chi tiết yêu cầu nghiệp vụ
2. **Lab 2 — Mô hình hóa yêu cầu:** Usecase Diagram, đặc tả Usecase, Activity Diagram, Sequence Diagram, Class Diagram, Statechart Diagram, Deployment Diagram
3. **Lab 3 — Thiết kế dữ liệu:** sơ đồ logic, chi tiết các bảng, câu lệnh SQL theo biểu mẫu
4. **Lab 4 — Thiết kế giao diện:** tiêu chuẩn thiết kế màn hình, sơ đồ giao diện tổng quát, giao diện chi tiết từng màn hình

## License

Dự án phục vụ mục đích học tập.
