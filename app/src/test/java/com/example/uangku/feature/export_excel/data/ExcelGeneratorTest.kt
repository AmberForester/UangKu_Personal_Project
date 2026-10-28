package com.example.uangku.feature.export_excel.data

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.transaction.domain.Transaction
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.Date
import java.util.zip.ZipFile

class ExcelGeneratorTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val generator = ExcelGenerator()

    @Test
    fun `generate produces valid xlsx zip with all required OpenXML entries`() {
        val outputFile = File(tempFolder.root, "test_export.xlsx")
        val sampleTransactions = listOf(
            Transaction(
                id = 1L,
                description = "Gaji & Bonus <Maret>",
                type = Type.INCOME,
                amount = 10000000.0,
                categoryId = 1,
                categoryName = "Gaji",
                date = Date()
            ),
            Transaction(
                id = 2L,
                description = "Makan Siang 'Restoran' \"Enak\"",
                type = Type.EXPENSE,
                amount = 50000.0,
                categoryId = 2,
                categoryName = "Makanan",
                date = Date()
            )
        )

        generator.generate(
            outputFile = outputFile,
            transactions = sampleTransactions,
            periodTitle = "Maret 2026"
        )

        assertTrue(outputFile.exists())
        assertTrue(outputFile.length() > 0)

        // Validate zip entries
        val zipFile = ZipFile(outputFile)
        val entryNames = zipFile.entries().asSequence().map { it.name }.toSet()

        assertTrue(entryNames.contains("[Content_Types].xml"))
        assertTrue(entryNames.contains("_rels/.rels"))
        assertTrue(entryNames.contains("xl/_rels/workbook.xml.rels"))
        assertTrue(entryNames.contains("xl/workbook.xml"))
        assertTrue(entryNames.contains("xl/styles.xml"))
        assertTrue(entryNames.contains("xl/worksheets/sheet1.xml"))

        // Validate sheet1 content
        val sheetEntry = zipFile.getEntry("xl/worksheets/sheet1.xml")
        assertNotNull(sheetEntry)
        val sheetXml = BufferedReader(InputStreamReader(zipFile.getInputStream(sheetEntry), Charsets.UTF_8)).readText()

        // XML escaping checks
        assertTrue(sheetXml.contains("Gaji &amp; Bonus &lt;Maret&gt;"))
        assertTrue(sheetXml.contains("Makan Siang &apos;Restoran&apos; &quot;Enak&quot;"))

        // Data checks
        assertTrue(sheetXml.contains("10000000"))
        assertTrue(sheetXml.contains("50000"))
        assertTrue(sheetXml.contains("Total Pemasukan"))
        assertTrue(sheetXml.contains("Total Pengeluaran"))
        assertTrue(sheetXml.contains("Saldo Bersih"))

        zipFile.close()
    }
}
