package com.example.data

import com.example.model.SqlLesson

object SqlLessonRepository {
    val exampleColumns = listOf("name", "department", "salary")
    val exampleRows = listOf(
        listOf("Ayu", "Engineering", "80000"),
        listOf("Bima", "Engineering", "60000"),
        listOf("Citra", "Sales", "70000"),
        listOf("Deni", "Engineering", "50000"),
        listOf("Eka", "Sales", "90000")
    )

    val basicSqlLessons: List<SqlLesson> = listOf(
        SqlLesson(
            keyword = "SELECT",
            title = "Pilih kolom yang ingin ditampilkan",
            explanation = "SELECT menentukan kolom yang muncul pada hasil query. Contohnya, ambil nama dan gaji karyawan tanpa menampilkan kolom department.",
            sql = "SELECT name, salary\nFROM employees;",
            resultColumns = listOf("name", "salary"),
            resultRows = listOf(
                listOf("Ayu", "80000"),
                listOf("Bima", "60000"),
                listOf("Citra", "70000"),
                listOf("Deni", "50000"),
                listOf("Eka", "90000")
            ),
            resultExplanation = "Kelima karyawan tetap masuk. Yang berubah hanya kolom yang ditampilkan: name dan salary.",
            tip = "Pisahkan nama kolom dengan koma. SELECT * menampilkan semua kolom, tetapi menyebutkan kolom secara eksplisit membuat tujuan query lebih jelas."
        ),
        SqlLesson(
            keyword = "FROM",
            title = "Tentukan tabel sumber data",
            explanation = "FROM menentukan tabel yang dibaca. Pada contoh ini, employees adalah tabel sumbernya. SELECT dan FROM dipakai bersama: pilih kolom, lalu sebutkan tabelnya.",
            sql = "SELECT *\nFROM employees;",
            resultColumns = listOf("name", "department", "salary"),
            resultRows = listOf(
                listOf("Ayu", "Engineering", "80000"),
                listOf("Bima", "Engineering", "60000"),
                listOf("Citra", "Sales", "70000"),
                listOf("Deni", "Engineering", "50000"),
                listOf("Eka", "Sales", "90000")
            ),
            resultExplanation = "Tanda * mengambil semua kolom dari tabel employees. Nama tabel setelah FROM harus sesuai dengan tabel yang tersedia.",
            tip = "Urutan penulisan dimulai dengan SELECT lalu FROM. Secara logis, sumber data dari FROM ditentukan sebelum kolom SELECT dievaluasi."
        ),
        SqlLesson(
            keyword = "WHERE",
            title = "Saring baris sebelum ditampilkan",
            explanation = "WHERE hanya meloloskan baris yang memenuhi kondisi. Gunakan AND jika semua kondisi harus terpenuhi, atau OR jika cukup salah satunya.",
            sql = "SELECT name, salary\nFROM employees\nWHERE department = 'Engineering'\n  AND salary >= 60000;",
            resultColumns = listOf("name", "salary"),
            resultRows = listOf(
                listOf("Ayu", "80000"),
                listOf("Bima", "60000")
            ),
            resultExplanation = "Ayu dan Bima lolos karena berada di Engineering dengan gaji minimal 60000. Deni tidak lolos karena gajinya 50000.",
            tip = "Gunakan tanda petik tunggal untuk teks. Operator >= mencakup nilai batasnya, jadi gaji tepat 60000 tetap lolos."
        ),
        SqlLesson(
            keyword = "GROUP BY",
            title = "Ringkas data menjadi kelompok",
            explanation = "GROUP BY menggabungkan baris dengan nilai yang sama menjadi satu kelompok. Gunakan fungsi agregat seperti COUNT(*) untuk menghitung baris atau SUM(salary) untuk menjumlahkan gaji per department.",
            sql = "SELECT department,\n       COUNT(*) AS employee_count,\n       SUM(salary) AS total_salary\nFROM employees\nGROUP BY department;",
            resultColumns = listOf("department", "employee_count", "total_salary"),
            resultRows = listOf(
                listOf("Engineering", "3", "190000"),
                listOf("Sales", "2", "160000")
            ),
            resultExplanation = "Lima baris karyawan diringkas menjadi dua baris department. Engineering memiliki 3 karyawan, sedangkan Sales memiliki 2 karyawan.",
            tip = "Pada contoh ini, department harus ada di GROUP BY karena dipilih tanpa fungsi agregat. WHERE menyaring baris sebelum pengelompokan; HAVING dapat menyaring kelompok setelahnya."
        ),
        SqlLesson(
            keyword = "ORDER BY",
            title = "Urutkan hasil query",
            explanation = "ORDER BY mengurutkan hasil akhir. ASC mengurutkan dari kecil ke besar dan DESC dari besar ke kecil. Tambahkan kolom kedua untuk menentukan urutan saat nilainya sama.",
            sql = "SELECT name, salary\nFROM employees\nORDER BY salary DESC, name ASC;",
            resultColumns = listOf("name", "salary"),
            resultRows = listOf(
                listOf("Eka", "90000"),
                listOf("Ayu", "80000"),
                listOf("Citra", "70000"),
                listOf("Bima", "60000"),
                listOf("Deni", "50000")
            ),
            resultExplanation = "Eka tampil pertama karena gajinya paling besar. Jika ada gaji yang sama, nama akan diurutkan dari A ke Z.",
            tip = "Tanpa ORDER BY, urutan baris tidak dijamin. Urutan tabel contoh pada materi lain hanya ilustrasi, bukan jaminan urutan dari database."
        )
    )

    val combinedSql = """
        SELECT department,
               COUNT(*) AS employee_count,
               SUM(salary) AS total_salary
        FROM employees
        WHERE salary >= 60000
        GROUP BY department
        ORDER BY total_salary DESC, department ASC;
    """.trimIndent()

    val combinedResultColumns = listOf("department", "employee_count", "total_salary")
    val combinedResultRows = listOf(
        listOf("Sales", "2", "160000"),
        listOf("Engineering", "2", "140000")
    )
}
