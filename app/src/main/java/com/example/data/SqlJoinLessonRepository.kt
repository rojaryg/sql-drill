package com.example.data

import com.example.model.DifficultyFilter
import com.example.model.SqlCourse
import com.example.model.SqlExampleTable
import com.example.model.SqlLesson

object SqlJoinLessonRepository {
    val sourceTables = listOf(
        SqlExampleTable(
            name = "customers",
            columns = listOf("customer_id", "name"),
            rows = listOf(
                listOf("1", "Ayu"),
                listOf("2", "Bima"),
                listOf("3", "Citra"),
                listOf("4", "Deni")
            )
        ),
        SqlExampleTable(
            name = "orders",
            columns = listOf("order_id", "customer_id", "total", "status"),
            rows = listOf(
                listOf("101", "1", "120000", "Paid"),
                listOf("102", "1", "80000", "Pending"),
                listOf("103", "2", "150000", "Paid"),
                listOf("104", "3", "50000", "Pending")
            )
        )
    )

    val lessons: List<SqlLesson> = listOf(
        SqlLesson(
            keyword = "RELASI",
            title = "Kenali hubungan antartabel",
            explanation = "Setelah Basic SQL, kamu sudah bisa membaca satu tabel. Sekarang, informasi pelanggan ada di customers dan informasi pesanan ada di orders. Kolom customer_id menghubungkan keduanya: satu pelanggan dapat memiliki banyak pesanan.",
            sql = "SELECT order_id, customer_id\nFROM orders\nORDER BY order_id;",
            resultColumns = listOf("order_id", "customer_id"),
            resultRows = listOf(
                listOf("101", "1"),
                listOf("102", "1"),
                listOf("103", "2"),
                listOf("104", "3")
            ),
            resultExplanation = "Pesanan 101 dan 102 milik customer_id 1, yaitu Ayu. Bima dan Citra masing-masing memiliki satu pesanan, sedangkan Deni belum memiliki pesanan.",
            tip = "customers.customer_id adalah primary key: unik dan tidak NULL. orders.customer_id adalah foreign key yang merujuk ke pelanggan tersebut; nilainya dapat berulang karena satu pelanggan bisa memesan berkali-kali."
        ),
        SqlLesson(
            keyword = "INNER JOIN",
            title = "Ambil baris yang memiliki pasangan",
            explanation = "INNER JOIN hanya menampilkan pasangan baris yang memenuhi kondisi ON. Gunakan alias c untuk customers dan o untuk orders agar nama kolom jelas dan query lebih ringkas.",
            sql = "SELECT c.name, o.order_id, o.total\nFROM customers c\nINNER JOIN orders o\n  ON c.customer_id = o.customer_id\nORDER BY c.customer_id, o.order_id;",
            resultColumns = listOf("name", "order_id", "total"),
            resultRows = listOf(
                listOf("Ayu", "101", "120000"),
                listOf("Ayu", "102", "80000"),
                listOf("Bima", "103", "150000"),
                listOf("Citra", "104", "50000")
            ),
            resultExplanation = "Ayu muncul dua kali karena memiliki dua pesanan. Deni tidak muncul karena tidak memiliki pasangan di orders.",
            tip = "Selalu tulis kondisi pasangan yang sesuai. Menggabungkan tabel tanpa kondisi yang benar dapat menghasilkan kombinasi baris yang keliru. Nama pelanggan yang berulang setelah JOIN bisa sah karena relasinya satu ke banyak."
        ),
        SqlLesson(
            keyword = "LEFT JOIN",
            title = "Tetap tampilkan semua pelanggan",
            explanation = "LEFT JOIN mempertahankan semua baris dari tabel kiri, yaitu customers. Jika tidak ada pesanan yang cocok, kolom dari orders pada hasil akan bernilai NULL.",
            sql = "SELECT c.name, o.order_id, o.total\nFROM customers c\nLEFT JOIN orders o\n  ON c.customer_id = o.customer_id\nORDER BY c.customer_id, o.order_id;",
            resultColumns = listOf("name", "order_id", "total"),
            resultRows = listOf(
                listOf("Ayu", "101", "120000"),
                listOf("Ayu", "102", "80000"),
                listOf("Bima", "103", "150000"),
                listOf("Citra", "104", "50000"),
                listOf("Deni", "NULL", "NULL")
            ),
            resultExplanation = "Deni sekarang ikut tampil dengan order_id dan total bernilai NULL. NULL di sini menunjukkan tidak ada pesanan yang cocok, bukan pesanan dengan total 0.",
            tip = "Pilihan tabel kiri menentukan baris mana yang dipertahankan. customers LEFT JOIN orders menjawab pertanyaan tentang semua pelanggan, termasuk yang belum bertransaksi."
        ),
        SqlLesson(
            keyword = "IS NULL",
            title = "Cari pelanggan tanpa pesanan",
            explanation = "Gabungkan LEFT JOIN dengan WHERE ... IS NULL untuk mencari baris yang tidak memiliki pasangan. Periksa order_id karena primary key pesanan tidak NULL untuk pesanan yang benar-benar ada.",
            sql = "SELECT c.customer_id, c.name\nFROM customers c\nLEFT JOIN orders o\n  ON c.customer_id = o.customer_id\nWHERE o.order_id IS NULL\nORDER BY c.customer_id;",
            resultColumns = listOf("customer_id", "name"),
            resultRows = listOf(
                listOf("4", "Deni")
            ),
            resultExplanation = "Hanya Deni yang lolos. Ayu, Bima, dan Citra memiliki pesanan sehingga order_id mereka tidak NULL.",
            tip = "Gunakan IS NULL, bukan = NULL. Hindari memeriksa kolom yang boleh NULL pada baris pesanan asli, karena itu bisa keliru dianggap sebagai tidak adanya pasangan."
        ),
        SqlLesson(
            keyword = "AGREGASI",
            title = "Ringkas pesanan per pelanggan",
            explanation = "Setelah JOIN, gunakan GROUP BY yang sudah kamu pelajari untuk membuat ringkasan per pelanggan. COUNT(o.order_id) menghitung pesanan, dan COALESCE mengganti hasil SUM yang NULL menjadi 0.",
            sql = "SELECT c.name,\n       COUNT(o.order_id) AS order_count,\n       COALESCE(SUM(o.total), 0) AS total_spent\nFROM customers c\nLEFT JOIN orders o\n  ON c.customer_id = o.customer_id\nGROUP BY c.customer_id, c.name\nORDER BY total_spent DESC, c.customer_id;",
            resultColumns = listOf("name", "order_count", "total_spent"),
            resultRows = listOf(
                listOf("Ayu", "2", "200000"),
                listOf("Bima", "1", "150000"),
                listOf("Citra", "1", "50000"),
                listOf("Deni", "0", "0")
            ),
            resultExplanation = "Ayu memiliki dua pesanan dengan total 200000. Deni tetap tampil dengan 0 pesanan dan total 0. Pengelompokan menggunakan ID dan nama agar pelanggan berbeda dengan nama yang sama tidak tercampur.",
            tip = "COUNT(*) menghitung semua baris hasil JOIN, termasuk baris Deni yang tidak punya pesanan, sehingga menghasilkan 1. COUNT(o.order_id) mengabaikan NULL sehingga menghasilkan 0 yang benar."
        ),
        SqlLesson(
            keyword = "ON vs WHERE",
            title = "Letakkan filter sesuai tujuan",
            explanation = "Jika semua pelanggan harus tetap tampil tetapi hanya pesanan Paid yang ingin dicocokkan, letakkan filter status di ON. Kondisi ON menentukan pasangan; WHERE menyaring hasil setelah JOIN.",
            sql = "SELECT c.name, o.order_id, o.status\nFROM customers c\nLEFT JOIN orders o\n  ON c.customer_id = o.customer_id\n AND o.status = 'Paid'\nORDER BY c.customer_id, o.order_id;",
            resultColumns = listOf("name", "order_id", "status"),
            resultRows = listOf(
                listOf("Ayu", "101", "Paid"),
                listOf("Bima", "103", "Paid"),
                listOf("Citra", "NULL", "NULL"),
                listOf("Deni", "NULL", "NULL")
            ),
            resultExplanation = "Ayu dan Bima memiliki pesanan Paid. Citra hanya memiliki pesanan Pending, dan Deni belum memiliki pesanan; keduanya tetap tampil dengan kolom pesanan NULL.",
            tip = "Jika o.status = 'Paid' dipindahkan ke WHERE, baris Citra dan Deni tersaring sehingga hanya Ayu dan Bima yang tersisa. Pilih ON atau WHERE berdasarkan apakah pelanggan tanpa pesanan Paid harus dipertahankan."
        )
    )

    val combinedExample =
        SqlLesson(
            keyword = "RANGKUMAN",
            title = "Ringkasan pesanan Paid untuk semua pelanggan",
            explanation = "Gabungkan LEFT JOIN, filter pada ON, GROUP BY, dan ORDER BY untuk melihat jumlah serta total pesanan Paid setiap pelanggan, termasuk yang belum punya pesanan Paid.",
            sql = "SELECT c.name,\n       COUNT(o.order_id) AS paid_orders,\n       COALESCE(SUM(o.total), 0) AS paid_total\nFROM customers c\nLEFT JOIN orders o\n  ON c.customer_id = o.customer_id\n AND o.status = 'Paid'\nGROUP BY c.customer_id, c.name\nORDER BY paid_total DESC, c.customer_id;",
            resultColumns = listOf("name", "paid_orders", "paid_total"),
            resultRows = listOf(
                listOf("Bima", "1", "150000"),
                listOf("Ayu", "1", "120000"),
                listOf("Citra", "0", "0"),
                listOf("Deni", "0", "0")
            ),
            resultExplanation = "Bima berada di urutan pertama dengan total Paid 150000, disusul Ayu 120000. Citra dan Deni tetap masuk dengan nilai 0, karena filter status berada di ON.",
            tip = "Mulai dari pertanyaannya: semua pelanggan atau hanya yang bertransaksi? Lalu tentukan JOIN, kondisi pasangan, filter, dan agregasi yang sesuai."
        )

    val course = SqlCourse(
        id = "joins",
        title = "JOIN",
        description = "Lanjutan Basic SQL: hubungkan data dari beberapa tabel, lalu buat ringkasan yang tepat.",
        prerequisite = "Bekal: SELECT, FROM, WHERE, GROUP BY, dan ORDER BY dari halaman Basic SQL.",
        sourceTables = sourceTables,
        lessons = lessons,
        combinedExample = combinedExample,
        recap = listOf(
            "INNER JOIN: hanya pasangan yang cocok. LEFT JOIN: semua baris kiri tetap ada.",
            "Satu pelanggan dapat muncul berkali-kali jika memiliki banyak pesanan.",
            "Cari data tanpa pasangan dengan LEFT JOIN dan IS NULL pada primary key tabel kanan.",
            "COUNT(kolom kanan) mengabaikan NULL; COALESCE dapat mengganti hasil agregat NULL dengan 0.",
            "Filter ON menentukan pasangan. Filter WHERE menyaring hasil JOIN.",
            "Latihan Intermediate berisi 10 soal campuran tingkat menengah, termasuk JOIN dan agregasi."
        ),
        practiceFilter = DifficultyFilter.INTERMEDIATE
    )
}
