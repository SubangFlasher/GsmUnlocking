package com.example.data

import com.example.model.Product
import com.example.model.ProductType
import com.example.model.ProductVariant
import com.example.model.ServerStatusItem
import com.example.model.TechnicianStatus

object SampleData {

    val aktivasiProducts = listOf(
        Product(
            id = "akt_unlocktool",
            name = "UnlockTool Activation",
            subtitle = "Tool Multi-Brand Terbaik untuk Teknisi HP",
            description = "Aktivasi resmi akun UnlockTool untuk bypass FRP, Mi Cloud Clean, Auth Flash Xiaomi Qualcomm & MTK, format userlock, Samsung Knox, iPad/iPhone Ramdisk bypass.",
            type = ProductType.AKTIVASI,
            category = "Multi-Brand",
            badge = "TERLARIS",
            availableSlots = 99,
            processTime = "5 - 10 Menit Otomatis",
            requiresAccountField = "Username Akun UnlockTool & Email Terdaftar",
            variants = listOf(
                ProductVariant("ut_3m", "3 Bulan", 325000L, 350000L, "Hemat untuk uji coba"),
                ProductVariant("ut_6m", "6 Bulan", 495000L, 550000L, "Paling Populer"),
                ProductVariant("ut_12m", "12 Bulan (1 Tahun)", 740000L, 820000L, "Super Hemat 1 Tahun")
            ),
            features = listOf(
                "Mendukung ribuan tipe Xiaomi, Samsung, Oppo, Vivo, Huawei, Infinix",
                "Fitur EDL 9008 Auth Flash gratis tanpa kredit tambahan",
                "Mi Cloud Clean & Bypass Micloud Tam/Distri",
                "Bypass FRP Samsung Android 11 s/d Android 15 OneUI 6/7",
                "Read/Write RPMB, Unlock Bootloader instan MTK"
            ),
            supportedBrands = listOf("Xiaomi", "Samsung", "Oppo", "Vivo", "Realme", "Infinix", "Tecno", "Huawei", "Apple")
        ),
        Product(
            id = "akt_chimeratool",
            name = "Chimera Tool PRO License",
            subtitle = "Software Master Servis Samsung & Flagship Brand",
            description = "Lisensi resmi Chimera Tool untuk perbaikan IMEI permanen, Patch Certificate, Read Codes, Knox Guard removal, dan unbrick berbagai smartphone flagship.",
            type = ProductType.AKTIVASI,
            category = "Samsung & Flagship",
            badge = "OFFICIAL RESELLER",
            availableSlots = 50,
            processTime = "5 - 15 Menit",
            requiresAccountField = "Username Akun Chimera Tool",
            variants = listOf(
                ProductVariant("ch_samsung", "Samsung License (1 Tahun)", 1150000L, 1250000L, "Khusus semua seri Samsung"),
                ProductVariant("ch_pro", "PRO All-Brand (1 Tahun)", 2350000L, 2500000L, "Full Samsung + Xiaomi + MTK + Huawei")
            ),
            features = listOf(
                "Patch Certificate & Repair IMEI Samsung Exynos & Snapdragon",
                "Bypass Knox Guard (KG Locked) status Completed",
                "Read Unlock Codes secara direct tanpa root",
                "Support update software terbaru mingguan dari server Chimera"
            ),
            supportedBrands = listOf("Samsung", "Huawei", "Xiaomi", "LG", "BlackBerry", "Motorola")
        ),
        Product(
            id = "akt_pandora",
            name = "Pandora Box Digital Tool",
            subtitle = "Spesialis Mediatek, Spreadtrum & Unisoc Terkuat",
            description = "Aktivasi lisensi digital Pandora Tool 1 Tahun. Sangat handal untuk menangani chipset Mediatek MT67xx/MT68xx dan Unisoc T606/T612/T616 yang terkunci.",
            type = ProductType.AKTIVASI,
            category = "MTK & Unisoc",
            badge = "HOT",
            availableSlots = 40,
            processTime = "10 - 20 Menit",
            requiresAccountField = "Email Akun Pandora / Z3X",
            variants = listOf(
                ProductVariant("pan_12m", "1 Tahun Lisensi Digital", 1480000L, 1600000L, "Tanpa butuh box fisik")
            ),
            features = listOf(
                "Direct bypass keamanan HP Vivo MTK terbaru",
                "Repair IMEI Unisoc/Spreadtrum tanpa bongkar mesin",
                "Format userdata + reset FRP dalam 1-klik",
                "Backup & Restore NVRAM / NVDATA file aman"
            ),
            supportedBrands = listOf("Vivo", "Realme", "Infinix", "Tecno", "Oppo", "ZTE")
        ),
        Product(
            id = "akt_dftpro",
            name = "DFT PRO Tool Activation",
            subtitle = "Solusi Dual SIM Repair Xiaomi & Vivo Auth",
            description = "Aktivasi akun DFT PRO Tool 1 Tahun. Populer di kalangan teknisi Indonesia untuk eksekusi Xiaomi MediaTek & Qualcomm dual hardware repair.",
            type = ProductType.AKTIVASI,
            category = "Xiaomi Specialist",
            badge = "REKOMENDASI",
            availableSlots = 35,
            processTime = "5 - 15 Menit",
            requiresAccountField = "Username Akun DFT PRO",
            variants = listOf(
                ProductVariant("dft_12m", "1 Tahun (Full Access)", 980000L, 1100000L, "Update server gratis 1 tahun")
            ),
            features = listOf(
                "Xiaomi Dual SIM Hardware IMEI write via fastboot/meta mode",
                "Disable OTA update otomatis setelah unlock",
                "Bypass Mi Account sensor aktif normal",
                "Oppo & Realme Format Factory New Security"
            ),
            supportedBrands = listOf("Xiaomi", "Redmi", "Poco", "Vivo", "Oppo", "Huawei")
        ),
        Product(
            id = "akt_borneo",
            name = "Borneo Schematics Skema HP",
            subtitle = "Database Skema Jalur & Hardware Solution No. 1",
            description = "Aktivasi skema sirkuit PCB smartphone terlengkap di dunia buatan Indonesia. Dilengkapi jalur tegangan hardware, bitmap interaktif, dan panduan pengukuran diode mode.",
            type = ProductType.AKTIVASI,
            category = "Hardware & Skema",
            badge = "WAJIB TEKNISI",
            availableSlots = 120,
            processTime = "Instan 5 Menit",
            requiresAccountField = "Username Akun Borneo Schematics",
            variants = listOf(
                ProductVariant("bor_1u", "1 User (12 Bulan)", 350000L, 380000L, "1 PC aktif"),
                ProductVariant("bor_2u", "2 User (12 Bulan)", 650000L, 700000L, "2 PC bersamaan (Hemat)")
            ),
            features = listOf(
                "Ribuan diagram jalur hardware iPhone, Samsung, Xiaomi, Oppo, Vivo",
                "Bitmap interaktif dengan nama komponen dan jalur putus",
                "Panduan nilai tegangan dan resistansi per titik test point",
                "Update skema HP terbaru setiap hari tanpa biaya tambahan"
            ),
            supportedBrands = listOf("iPhone", "iPad", "MacBook", "Samsung", "Xiaomi", "Oppo", "Vivo", "Laptop")
        ),
        Product(
            id = "akt_hydratool",
            name = "Hydra Tool Digital Dongle",
            subtitle = "Quad-Engine Flash & Unlock Qualcomm / MTK",
            description = "Aktivasi paket modul digital Hydra Tool. Mendukung Qualcomm Tool, MediaTek Tool, Spreadtrum Tool, dan EMMC Tool.",
            type = ProductType.AKTIVASI,
            category = "Dongle Digital",
            badge = "STABLE",
            availableSlots = 25,
            processTime = "15 Menit",
            requiresAccountField = "Serial Number Hydra / Username",
            variants = listOf(
                ProductVariant("hyd_6m", "6 Bulan License", 420000L, 460000L, "Paket Menengah"),
                ProductVariant("hyd_12m", "12 Bulan License", 690000L, 750000L, "Paket Lengkap 1 Tahun")
            ),
            features = listOf(
                "Koleksi Loader EDL Qualcomm terlengkap",
                "Support flashing Xiaomi firmware fastboot & EDL",
                "FRP Reset & Demo mode remover"
            ),
            supportedBrands = listOf("Qualcomm", "MediaTek", "Unisoc", "Xiaomi", "Nokia")
        ),
        Product(
            id = "akt_pragmafix",
            name = "Pragmafix Schematics",
            subtitle = "Solusi Skema & Panduan Belajar Hardware Interaktif",
            description = "Aktivasi lisensi Pragmafix 1 Tahun. Menampilkan panduan step-by-step analisa kerusakan mesin HP dan laptop.",
            type = ProductType.AKTIVASI,
            category = "Hardware & Skema",
            badge = null,
            availableSlots = 40,
            processTime = "5 - 10 Menit",
            requiresAccountField = "Email Akun Pragmafix",
            variants = listOf(
                ProductVariant("prg_12m", "12 Bulan Multi-PC", 380000L, 420000L, "Akses ribuan panduan servis")
            ),
            features = listOf(
                "Panduan analisa tegangan buck & LDO power management IC",
                "Database nilai hambatan dalam (diode value)",
                "Jalur kamera, charging, sinyal, audio"
            ),
            supportedBrands = listOf("Android", "iPhone", "Laptop Motherboard")
        )
    )

    val sewaProducts = listOf(
        Product(
            id = "sewa_unlocktool",
            name = "Sewa UnlockTool Server",
            subtitle = "Slot Rental Akun UnlockTool Siap Pakai",
            description = "Sewa akun UnlockTool resmi untuk eksekusi cepat tanpa perlu beli lisensi tahunan. Diberikan kredensial login langsung ke software UnlockTool di PC teknisi.",
            type = ProductType.SEWA,
            category = "Sewa Populer",
            badge = "TERLARIS",
            availableSlots = 14,
            processTime = "Langsung Aktif (1-3 Menit)",
            requiresAccountField = "Nomor WhatsApp untuk Terima Login",
            variants = listOf(
                ProductVariant("sw_ut_2h", "2 Jam Rental", 20000L, 25000L, "Cocok untuk 1-2 nota HP"),
                ProductVariant("sw_ut_6h", "6 Jam Rental", 35000L, 45000L, "Paket Setengah Hari"),
                ProductVariant("sw_ut_12h", "12 Jam Rental", 55000L, 70000L, "Paket 1 Shift Kerja Penuh"),
                ProductVariant("sw_ut_24h", "24 Jam (1 Hari)", 80000L, 100000L, "Bebas Pakai Seharian")
            ),
            features = listOf(
                "Akun resmi premium bebas limit eksekusi",
                "Kredensial username & password dikirim via invoice/WA instan",
                "Support semua fitur: FRP, Micloud, Auth Flash, Factory Reset",
                "Dilarang logout atau pindah PC sebelum durasi habis"
            ),
            supportedBrands = listOf("Xiaomi", "Samsung", "Oppo", "Vivo", "Realme", "Infinix")
        ),
        Product(
            id = "sewa_chimeratool",
            name = "Sewa Chimera Tool PRO",
            subtitle = "Sewa Slot PC Chimera Master Repair",
            description = "Sewa akun Chimera Tool PRO untuk perbaikan IMEI Samsung, repair certificate, read codes, atau unlock jaringan. Terkoneksi via remote desktop / binding PC.",
            type = ProductType.SEWA,
            category = "Sewa Flagship",
            badge = "EXCLUSIVE",
            availableSlots = 6,
            processTime = "Proses 5 Menit",
            requiresAccountField = "Nama PC / UltraViewer ID untuk Binding",
            variants = listOf(
                ProductVariant("sw_ch_3h", "3 Jam Rental", 45000L, 55000L, "Untuk 1-3 unit Samsung"),
                ProductVariant("sw_ch_6h", "6 Jam Rental", 75000L, 90000L, "Paket Standar Teknisi"),
                ProductVariant("sw_ch_12h", "12 Jam Rental", 120000L, 150000L, "Full Day Session"),
                ProductVariant("sw_ch_24h", "24 Jam Rental", 180000L, 220000L, "Puas 24 Jam Penuh")
            ),
            features = listOf(
                "Support Patch Cert Samsung Exynos & Qualcomm",
                "Bypass Knox Guard status Completed",
                "Termasuk panduan koneksi dan driver Chimera"
            ),
            supportedBrands = listOf("Samsung", "Huawei", "Xiaomi")
        ),
        Product(
            id = "sewa_pandora",
            name = "Sewa Pandora Tool Digital",
            subtitle = "Solusi Cepat HP Vivo & Chipset MTK/Unisoc",
            description = "Sewa akun Pandora Tool Digital untuk mengatasi HP Vivo security 2024-2025 yang bandel, format userdata tanpa auth, dan repair IMEI chipset Unisoc.",
            type = ProductType.SEWA,
            category = "Sewa MTK",
            badge = "READY SLOT",
            availableSlots = 8,
            processTime = "3 Menit Siap",
            requiresAccountField = "Nomor WhatsApp Pemesan",
            variants = listOf(
                ProductVariant("sw_pan_3h", "3 Jam Rental", 40000L, 50000L, "Cukup untuk beberapa unit"),
                ProductVariant("sw_pan_6h", "6 Jam Rental", 65000L, 80000L, "Paket Rekomendasi"),
                ProductVariant("sw_pan_12h", "12 Jam Rental", 110000L, 130000L, "Paket Seharian")
            ),
            features = listOf(
                "Support Vivo Y-series & V-series security baru",
                "Bypass FRP Unisoc T606/T612 instan",
                "Akun server resmi stabil tanpa gangguan"
            ),
            supportedBrands = listOf("Vivo", "Realme", "Infinix", "Tecno")
        ),
        Product(
            id = "sewa_dftpro",
            name = "Sewa DFT PRO Tool",
            subtitle = "Sewa Akun DFT PRO Khusus Xiaomi & Vivo",
            description = "Butuh tulis IMEI Xiaomi dual hardware atau unlock Mi Account instan? Sewa slot DFT PRO dengan harga terjangkau per jam.",
            type = ProductType.SEWA,
            category = "Sewa Xiaomi",
            badge = "HEMAT",
            availableSlots = 10,
            processTime = "3 Menit Siap",
            requiresAccountField = "Nomor WhatsApp Pemesan",
            variants = listOf(
                ProductVariant("sw_dft_2h", "2 Jam Rental", 25000L, 30000L, "Eksekusi kilat"),
                ProductVariant("sw_dft_6h", "6 Jam Rental", 45000L, 55000L, "Shift Siang/Malam"),
                ProductVariant("sw_dft_12h", "12 Jam Rental", 75000L, 95000L, "12 Jam Puas")
            ),
            features = listOf(
                "Xiaomi Dual IMEI Repair support",
                "Disable OTA and Micloud Sensor Patch",
                "Fastboot to EDL modded loader support"
            ),
            supportedBrands = listOf("Xiaomi", "Redmi", "Poco", "Vivo")
        ),
        Product(
            id = "sewa_borneo",
            name = "Sewa Borneo Schematics PC",
            subtitle = "Akses Skema HP Darurat untuk Cek Jalur PCB",
            description = "Solusi hemat saat ada servis mesin masuk dan butuh cek jalur hardware tegangan tanpa harus beli lisensi 1 tahun penuh.",
            type = ProductType.SEWA,
            category = "Sewa Skema",
            badge = "POPULER",
            availableSlots = 20,
            processTime = "Instan",
            requiresAccountField = "Nomor WhatsApp Pemesan",
            variants = listOf(
                ProductVariant("sw_bor_1d", "1 Hari (24 Jam)", 15000L, 20000L, "Paling Murah"),
                ProductVariant("sw_bor_3d", "3 Hari (72 Jam)", 35000L, 45000L, "Sangat Hemat 3 Hari"),
                ProductVariant("sw_bor_7d", "7 Hari (1 Minggu)", 70000L, 90000L, "Akses Penuh 1 Minggu")
            ),
            features = listOf(
                "Akses semua diagram skema & bitmap Borneo",
                "Tinggal login di software Borneo Schematics",
                "Mudah dipakai untuk analisa HP konslet / short"
            ),
            supportedBrands = listOf("Semua Skema Smartphone & Laptop")
        )
    )

    val remoteProducts = listOf(
        Product(
            id = "rem_samsung_frp",
            name = "Remote Bypass FRP Samsung All Series",
            subtitle = "Android 11, 12, 13, 14, 15 & Knox Guard",
            description = "Jasa bypass akun Google (FRP) Samsung terkunci setelah reset pabrik. Dikerjakan langsung oleh teknisi master kami via UltraViewer / AnyDesk.",
            type = ProductType.REMOTE,
            category = "Bypass FRP",
            badge = "TERLARIS",
            availableSlots = 15,
            processTime = "5 - 15 Menit Pengerjaan",
            requiresAccountField = "Tipe HP, Patch Security & ID UltraViewer",
            variants = listOf(
                ProductVariant("rem_sam_frp_std", "FRP Samsung Standar (All Android)", 85000L, 100000L, "Selesai 5-10 menit"),
                ProductVariant("rem_sam_frp_flag", "FRP Flagship S/Z Series 2024-2025", 130000L, 150000L, "Security Patch Terbaru")
            ),
            features = listOf(
                "Tidak perlu bongkar mesin / tanpa test point",
                "Hanya butuh kabel data original & PC Windows",
                "Garansi uang kembali jika gagal eksekusi",
                "Pengerjaan dipandu langsung oleh teknisi sampai normal"
            ),
            supportedBrands = listOf("Samsung Galaxy A-Series", "M-Series", "S-Series", "Z-Flip/Fold")
        ),
        Product(
            id = "rem_micloud_resmi",
            name = "Remote Clean Mi Cloud Server Resmi",
            subtitle = "Bypass Akun Mi Xiaomi Tam & Distributor",
            description = "Pembersihan Mi Cloud langsung dari server database Xiaomi resmi (Clean Lost / Tam). Setelah selesai HP bisa login akun Mi baru, reset bebas, dan update OTA aman.",
            type = ProductType.REMOTE,
            category = "Mi Cloud",
            badge = "GARANSI CLEAN",
            availableSlots = 20,
            processTime = "10 Menit s/d 2 Jam",
            requiresAccountField = "Nomor Unlock Code di Layar Terkunci",
            variants = listOf(
                ProductVariant("rem_mi_distri", "Mi Cloud Clean Distributor / Luar", 125000L, 150000L, "Fast server clean"),
                ProductVariant("rem_mi_tam", "Mi Cloud Clean TAM Resmi Indonesia", 185000L, 220000L, "Clean Server Permanen Bebas Reset")
            ),
            features = listOf(
                "Cukup kirim foto layar gembok beserta kode unlocknya",
                "Permanen! Bebas update HyperOS / MIUI & bebas reset selamanya",
                "Bisa login akun Mi baru milik sendiri",
                "Status di server Xiaomi resmi menjadi UNLOCKED / CLEAN"
            ),
            supportedBrands = listOf("Xiaomi", "Redmi", "Poco Series")
        ),
        Product(
            id = "rem_imei_repair",
            name = "Remote Repair IMEI & Sinyal Permanen",
            subtitle = "Pulihkan Sinyal Hilang, Bulat/No Service",
            description = "Perbaikan IMEI yang hilang/null/0049 atau sinyal terblokir. Menggunakan tool resmi server agar kedua slot SIM Card aktif kembali untuk operator Indonesia.",
            type = ProductType.REMOTE,
            category = "Repair Sinyal",
            badge = "SINYAL ON",
            availableSlots = 10,
            processTime = "15 - 30 Menit",
            requiresAccountField = "Merk, Tipe HP & ID UltraViewer / AnyDesk",
            variants = listOf(
                ProductVariant("rem_imei_mtk", "Dual SIM MediaTek / Unisoc", 150000L, 180000L, "Permanen semua operator"),
                ProductVariant("rem_imei_qual", "Dual SIM Qualcomm Snapdragon", 200000L, 250000L, "Patch QCN & Security Server")
            ),
            features = listOf(
                "Sinyal 4G / 5G kembali aktif normal (Telkomsel, Indosat, XL, Tri, Smartfren)",
                "Bisa telepon, SMS, dan internet data lancar",
                "Garansi sinyal stabil",
                "Dikerjakan via remote USB Redirector / UltraViewer"
            ),
            supportedBrands = listOf("Xiaomi", "Oppo", "Vivo", "Realme", "Infinix", "Samsung")
        ),
        Product(
            id = "rem_demo_live",
            name = "Remote Remove Demo Live All Brand",
            subtitle = "Hapus Mode Demo Toko / Retail Phone",
            description = "Layanan remote penghapusan Demo Live / Demo Mode pada smartphone display toko agar menjadi unit normal konsumen 100%.",
            type = ProductType.REMOTE,
            category = "Demo Remover",
            badge = "PERMANEN",
            availableSlots = 18,
            processTime = "10 Menit",
            requiresAccountField = "Merk & Tipe HP (Oppo/Vivo/Realme/Samsung)",
            variants = listOf(
                ProductVariant("rem_dm_ovr", "Demo Oppo / Vivo / Realme", 75000L, 90000L, "Semua tipe 2023-2025"),
                ProductVariant("rem_dm_sam", "Demo Live Samsung Retail Mode", 95000L, 115000L, "Layar normal permanen")
            ),
            features = listOf(
                "Aplikasi demo toko dan video display terhapus total",
                "Bebas reset pabrik dan update sistem operasi",
                "Kamera, fingerprint, dan sensor kembali aktif normal",
                "Pengerjaan cepat tanpa ribet"
            ),
            supportedBrands = listOf("Oppo", "Vivo", "Realme", "Samsung")
        ),
        Product(
            id = "rem_flash_unbrick",
            name = "Remote Flash Unbrick & Qualcomm 9008",
            subtitle = "Solusi HP Bootloop, Matot, Deadboot, Fastboot Loop",
            description = "Jasa flashing firmware original via EDL 9008 Auth Server resmi. Menyelamatkan smartphone yang bricked, gagal update, atau salah oprek file recovery.",
            type = ProductType.REMOTE,
            category = "Unbrick & Flash",
            badge = "AHLI FLASHER",
            availableSlots = 12,
            processTime = "20 - 45 Menit",
            requiresAccountField = "Kondisi HP & ID UltraViewer PC Teknisi",
            variants = listOf(
                ProductVariant("rem_fl_edl", "Flash EDL 9008 + Akun Auth Server", 120000L, 150000L, "Include token auth server"),
                ProductVariant("rem_fl_mtk", "Flash MTK BROM Unbrick", 95000L, 120000L, "Bypass SLA/DA Auth")
            ),
            features = listOf(
                "Menggunakan ROM Official Global / Indonesia",
                "Termasuk akun Auth server resmi jika tipe membutuhkan login server",
                "Bisa konsultasi testpoint titik poin mesin",
                "Data user diusahakan aman bila kondisi memungkinkan"
            ),
            supportedBrands = listOf("Xiaomi", "Redmi", "Oppo", "Realme", "Vivo", "OnePlus")
        ),
        Product(
            id = "rem_apple_bypass",
            name = "Remote Apple iPhone iCloud & Passcode",
            subtitle = "Bypass Hello Screen & Passcode iPhone 6 s/d X",
            description = "Jasa remote bypass layar Hello iCloud atau lupa sandi passcode iPhone/iPad. Termasuk sinyal aktif dan notifikasi lancar.",
            type = ProductType.REMOTE,
            category = "Apple iOS",
            badge = "SPECIALIST",
            availableSlots = 8,
            processTime = "15 - 30 Menit",
            requiresAccountField = "Model iPhone, Versi iOS & SN",
            variants = listOf(
                ProductVariant("rem_ip_pass", "Bypass Passcode (Sinyal On)", 140000L, 170000L, "iOS 14 - 16 Full Sinyal"),
                ProductVariant("rem_ip_hello", "Bypass Hello Screen iOS 15 - 16.7", 180000L, 210000L, "Support iCloud login baru")
            ),
            features = listOf(
                "Support iPhone 6s s/d iPhone X, iPad berbagai generasi",
                "iMessage, FaceTime, iCloud Login, Notifikasi normal",
                "Panduan DFU mode langsung dipandu oleh teknisi ahli"
            ),
            supportedBrands = listOf("iPhone 6s", "iPhone 7/7+", "iPhone 8/8+", "iPhone X", "iPad")
        )
    )

    val serverStatuses = listOf(
        ServerStatusItem("UnlockTool Server Auth", "Multi-Tool", "ONLINE", 28, "1 menit lalu"),
        ServerStatusItem("Chimera Authenticator", "Samsung & PRO", "ONLINE", 36, "3 menit lalu"),
        ServerStatusItem("Xiaomi Official EDL Auth", "Auth Flash", "NORMAL", 52, "Baru saja"),
        ServerStatusItem("Samsung KG Status Server", "Knox Guard", "ONLINE", 24, "2 menit lalu"),
        ServerStatusItem("Pandora Box Cloud API", "MTK/SPD", "ONLINE", 41, "4 menit lalu"),
        ServerStatusItem("DFT PRO Token Service", "Xiaomi/Vivo", "ONLINE", 30, "Baru saja"),
        ServerStatusItem("Borneo Schematics Server", "Hardware Cloud", "ONLINE", 19, "1 menit lalu")
    )

    val technicians = listOf(
        TechnicianStatus("Bang Budi (Master Unlocker)", "Spesialis Xiaomi & Samsung EDL Auth", true, "5.0 ★", 1420),
        TechnicianStatus("Kang Asep (GSM Solo)", "Spesialis Repair IMEI & Hardware Flasher", true, "4.9 ★", 980),
        TechnicianStatus("Mas Danang (Cyber Tech)", "Spesialis Apple iPhone Bypass & MTK", true, "4.9 ★", 740)
    )

    val initialReviews = listOf(
        com.example.model.ReviewItem(
            id = "rev_1",
            productId = "akt_unlocktool",
            authorName = "Rian Cell Flash",
            roleBadge = "Master Flasher",
            rating = 5,
            comment = "Mantap jiwa! Beli lisensi UnlockTool 12 bulan proses cuma 3 menit langsung aktif. Langsung gas garap Samsung A14 FRP done dalam hitungan detik. Recommended seller GSM UNLOCKING INDONESIA!",
            dateString = "2 hari yang lalu",
            deviceOrVariantUsed = "Lisensi 12 Bulan (1 Tahun)",
            isHelpfulCount = 24
        ),
        com.example.model.ReviewItem(
            id = "rev_2",
            productId = "akt_unlocktool",
            authorName = "Hendra Counter Surabaya",
            roleBadge = "Teknisi Terverifikasi",
            rating = 5,
            comment = "Harga paling bersaing dibanding tempat lain. CS ramah dan responsif saat tanya kendala driver MTK. Sukses terus bos!",
            dateString = "5 hari yang lalu",
            deviceOrVariantUsed = "Lisensi 6 Bulan",
            isHelpfulCount = 18
        ),
        com.example.model.ReviewItem(
            id = "rev_3",
            productId = "akt_chimeratool",
            authorName = "Bambang Solo GSM",
            roleBadge = "Spesialis Samsung",
            rating = 5,
            comment = "Chimera Samsung aktivasi kilat. Patch Cert seri Note 20 Ultra langsung tembus sinyal all operator. Sangat puas dengan kecepatan layanannya.",
            dateString = "1 minggu lalu",
            deviceOrVariantUsed = "Samsung License (1 Tahun)",
            isHelpfulCount = 15
        ),
        com.example.model.ReviewItem(
            id = "rev_4",
            productId = "sewa_unlocktool",
            authorName = "Dodi Flash Malang",
            roleBadge = "Pelanggan Terverifikasi",
            rating = 5,
            comment = "Sewa 2 jam penyelamat user dadakan! Akun langsung dikirim dan server SG lancar tanpa antre. Cuan 150rb cuma modal sewa 15rb.",
            dateString = "Kemarin",
            deviceOrVariantUsed = "Sewa 2 Jam Kilat",
            isHelpfulCount = 31
        ),
        com.example.model.ReviewItem(
            id = "rev_5",
            productId = "sewa_unlocktool",
            authorName = "Agus Service HP",
            roleBadge = "Teknisi Counter",
            rating = 4,
            comment = "Server lancar, akun langsung login. Saran durasi ditambah paket 3 jam biar makin leluasa backup rom. Overall mantap!",
            dateString = "3 hari yang lalu",
            deviceOrVariantUsed = "Sewa 6 Jam Santai",
            isHelpfulCount = 9
        ),
        com.example.model.ReviewItem(
            id = "rev_6",
            productId = "rem_samsung_frp",
            authorName = "Yoga Pratama",
            roleBadge = "Pelanggan Terverifikasi",
            rating = 5,
            comment = "Gila cepet banget! Pengerjaan remote bypass FRP Samsung A54 via UltraViewer ditangani Bang Budi cuma 5 menit selesai tanpa ribet. HP langsung normal masuk menu utama. Terimakasih!",
            dateString = "Kemarin",
            deviceOrVariantUsed = "Samsung Galaxy A54 5G (Android 14)",
            isHelpfulCount = 42
        ),
        com.example.model.ReviewItem(
            id = "rev_7",
            productId = "rem_xiaomi_micloud",
            authorName = "Fikri Service Center",
            roleBadge = "Teknisi Terverifikasi",
            rating = 5,
            comment = "Redmi Note 12 Pro terkunci akun Mi Cloud clean sukses di-remote Kang Asep via mode EDL 9008 auth. Hasil bersih tanpa relock saat connect wifi. Teknisi beneran master!",
            dateString = "4 hari yang lalu",
            deviceOrVariantUsed = "Xiaomi Redmi Note 12 Pro",
            isHelpfulCount = 27
        ),
        com.example.model.ReviewItem(
            id = "rev_8",
            productId = "akt_borneo",
            authorName = "Maman Hardware Tech",
            roleBadge = "Hardware Specialist",
            rating = 5,
            comment = "Aktivasi skema Borneo 1 tahun beres dalam 5 menit. Skematik jalur charging & audio lengkap banget buat tracing jalur mati total.",
            dateString = "1 minggu lalu",
            deviceOrVariantUsed = "1 User (1 Tahun)",
            isHelpfulCount = 14
        )
    )
}
