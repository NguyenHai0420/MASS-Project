import Sidebar from "./Sidebar";

// Layout chứa Sidebar bên trái + nội dung bên phải
// Dùng chung cho tất cả trang Doctor và Admin
function DashboardLayout({ children }) {
    return (
        <div style={{ display: "flex" }}>
            <Sidebar />
            <div style={{ flex: 1, padding: "20px", backgroundColor: "#f8f9fa", minHeight: "100vh" }}>
                {children}
            </div>
        </div>
    );
}

export default DashboardLayout;
