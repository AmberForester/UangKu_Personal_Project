package com.example.uangku.feature.export_excel.data

import com.example.uangku.core.domain.Type
import com.example.uangku.feature.transaction.domain.Transaction
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExcelGenerator {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))

    fun generate(
        outputFile: File,
        transactions: List<Transaction>,
        periodTitle: String
    ) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(outputFile))).use { zos ->
            // 1. [Content_Types].xml
            zos.putNextEntry(ZipEntry("[Content_Types].xml"))
            zos.write(getContentTypesXml().toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // 2. _rels/.rels
            zos.putNextEntry(ZipEntry("_rels/.rels"))
            zos.write(getRootRelsXml().toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // 3. xl/_rels/workbook.xml.rels
            zos.putNextEntry(ZipEntry("xl/_rels/workbook.xml.rels"))
            zos.write(getWorkbookRelsXml().toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // 4. xl/workbook.xml
            zos.putNextEntry(ZipEntry("xl/workbook.xml"))
            zos.write(getWorkbookXml().toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // 5. xl/styles.xml
            zos.putNextEntry(ZipEntry("xl/styles.xml"))
            zos.write(getStylesXml().toByteArray(Charsets.UTF_8))
            zos.closeEntry()

            // 6. xl/worksheets/sheet1.xml
            zos.putNextEntry(ZipEntry("xl/worksheets/sheet1.xml"))
            val writer = OutputStreamWriter(zos, Charsets.UTF_8)
            writeWorksheetXml(writer, transactions, periodTitle)
            writer.flush()
            zos.closeEntry()
        }
    }

    private fun getContentTypesXml(): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
  <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
  <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
</Types>""".trimIndent()

    private fun getRootRelsXml(): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>""".trimIndent()

    private fun getWorkbookRelsXml(): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
  <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>""".trimIndent()

    private fun getWorkbookXml(): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <sheets>
    <sheet name="Laporan Transaksi" sheetId="1" r:id="rId1"/>
  </sheets>
</workbook>""".trimIndent()

    private fun getStylesXml(): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
  <numFmts count="1">
    <numFmt numFmtId="164" formatCode="&quot;Rp &quot;#,##0"/>
  </numFmts>
  <fonts count="6">
    <!-- 0: Default Normal -->
    <font><sz val="11"/><name val="Calibri"/></font>
    <!-- 1: Header (bold, white) -->
    <font><b/><sz val="11"/><color rgb="FFFFFFFF"/><name val="Calibri"/></font>
    <!-- 2: Title (bold, 14pt) -->
    <font><b/><sz val="14"/><color rgb="FF0F172A"/><name val="Calibri"/></font>
    <!-- 3: Bold dark text -->
    <font><b/><sz val="11"/><color rgb="FF1E293B"/><name val="Calibri"/></font>
    <!-- 4: Bold income green (#15803D) -->
    <font><b/><sz val="11"/><color rgb="FF15803D"/><name val="Calibri"/></font>
    <!-- 5: Bold expense red (#B91C1C) -->
    <font><b/><sz val="11"/><color rgb="FFB91C1C"/><name val="Calibri"/></font>
  </fonts>
  <fills count="4">
    <fill><patternFill patternType="none"/></fill>
    <fill><patternFill patternType="gray125"/></fill>
    <!-- 2: Primary Sky Blue (#0284C7) -->
    <fill><patternFill patternType="solid"><fgColor rgb="FF0284C7"/><bgColor indexed="64"/></patternFill></fill>
    <!-- 3: Light Slate Accent (#F8FAFC) -->
    <fill><patternFill patternType="solid"><fgColor rgb="FFF8FAFC"/><bgColor indexed="64"/></patternFill></fill>
  </fills>
  <borders count="2">
    <border><left/><right/><top/><bottom/><diagonal/></border>
    <border>
      <left style="thin"><color rgb="FFE2E8F0"/></left>
      <right style="thin"><color rgb="FFE2E8F0"/></right>
      <top style="thin"><color rgb="FFE2E8F0"/></top>
      <bottom style="thin"><color rgb="FFE2E8F0"/></bottom>
      <diagonal/>
    </border>
  </borders>
  <cellStyleXfs count="1">
    <xf numFmtId="0" fontId="0" fillId="0" borderId="0"/>
  </cellStyleXfs>
  <cellXfs count="10">
    <!-- 0: Normal -->
    <xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/>
    <!-- 1: Table Header -->
    <xf numFmtId="0" fontId="1" fillId="2" borderId="1" xfId="0" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="center" vertical="center"/>
    </xf>
    <!-- 2: Data text (left) -->
    <xf numFmtId="0" fontId="0" fillId="0" borderId="1" xfId="0" applyFont="1" applyBorder="1" applyAlignment="1">
      <alignment vertical="center"/>
    </xf>
    <!-- 3: Data text (center) -->
    <xf numFmtId="0" fontId="0" fillId="0" borderId="1" xfId="0" applyFont="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="center" vertical="center"/>
    </xf>
    <!-- 4: Data currency (right) -->
    <xf numFmtId="164" fontId="0" fillId="0" borderId="1" xfId="0" applyNumberFormat="1" applyFont="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="right" vertical="center"/>
    </xf>
    <!-- 5: App Title -->
    <xf numFmtId="0" fontId="2" fillId="0" borderId="0" xfId="0" applyFont="1"/>
    <!-- 6: Summary Row Label -->
    <xf numFmtId="0" fontId="3" fillId="3" borderId="1" xfId="0" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="right" vertical="center"/>
    </xf>
    <!-- 7: Summary Row Currency -->
    <xf numFmtId="164" fontId="3" fillId="3" borderId="1" xfId="0" applyNumberFormat="1" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="right" vertical="center"/>
    </xf>
    <!-- 8: Summary Income Currency -->
    <xf numFmtId="164" fontId="4" fillId="3" borderId="1" xfId="0" applyNumberFormat="1" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="right" vertical="center"/>
    </xf>
    <!-- 9: Summary Expense Currency -->
    <xf numFmtId="164" fontId="5" fillId="3" borderId="1" xfId="0" applyNumberFormat="1" applyFont="1" applyFill="1" applyBorder="1" applyAlignment="1">
      <alignment horizontal="right" vertical="center"/>
    </xf>
  </cellXfs>
  <cellStyles count="1">
    <cellStyle name="Normal" xfId="0" builtinId="0"/>
  </cellStyles>
</styleSheet>""".trimIndent()

    private fun writeWorksheetXml(
        writer: OutputStreamWriter,
        transactions: List<Transaction>,
        periodTitle: String
    ) {
        val totalCount = transactions.size
        val lastDataRow = 4 + totalCount
        val totalIncome = transactions.filter { it.type == Type.INCOME }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == Type.EXPENSE }.sumOf { it.amount }
        val netBalance = totalIncome - totalExpense
        val lastRow = lastDataRow + 3

        writer.write("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        writer.write("""<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">""")
        writer.write("""<dimension ref="A1:F$lastRow"/>""")
        writer.write("""<sheetViews><sheetView tabSelected="1" workbookViewId="0"><pane ySplit="4" topLeftCell="A5" activePane="bottomLeft" state="frozen"/></sheetView></sheetViews>""")
        writer.write("""<sheetFormatPr defaultRowHeight="20"/>""")
        writer.write("""<cols>""")
        writer.write("""<col min="1" max="1" width="8" customWidth="1"/>""")
        writer.write("""<col min="2" max="2" width="16" customWidth="1"/>""")
        writer.write("""<col min="3" max="3" width="16" customWidth="1"/>""")
        writer.write("""<col min="4" max="4" width="22" customWidth="1"/>""")
        writer.write("""<col min="5" max="5" width="32" customWidth="1"/>""")
        writer.write("""<col min="6" max="6" width="22" customWidth="1"/>""")
        writer.write("""</cols>""")
        writer.write("""<sheetData>""")

        // Row 1: Title
        writer.write("""<row r="1" ht="28" customHeight="1">""")
        writer.write("""<c r="A1" s="5" t="inlineStr"><is><t>LAPORAN TRANSAKSI UANGKU</t></is></c>""")
        writer.write("""</row>""")

        // Row 2: Subtitle
        writer.write("""<row r="2" ht="20" customHeight="1">""")
        writer.write("""<c r="A2" t="inlineStr"><is><t>Periode: ${escapeXml(periodTitle)}</t></is></c>""")
        writer.write("""</row>""")

        // Row 3: Spacing
        writer.write("""<row r="3" ht="10" customHeight="1"/>""")

        // Row 4: Header
        writer.write("""<row r="4" ht="26" customHeight="1">""")
        writer.write("""<c r="A4" s="1" t="inlineStr"><is><t>No</t></is></c>""")
        writer.write("""<c r="B4" s="1" t="inlineStr"><is><t>Tanggal</t></is></c>""")
        writer.write("""<c r="C4" s="1" t="inlineStr"><is><t>Tipe</t></is></c>""")
        writer.write("""<c r="D4" s="1" t="inlineStr"><is><t>Kategori</t></is></c>""")
        writer.write("""<c r="E4" s="1" t="inlineStr"><is><t>Catatan</t></is></c>""")
        writer.write("""<c r="F4" s="1" t="inlineStr"><is><t>Nominal (Rp)</t></is></c>""")
        writer.write("""</row>""")

        // Rows 5..4+N: Data
        for ((index, tx) in transactions.withIndex()) {
            val rowNum = 5 + index
            val typeStr = if (tx.type == Type.INCOME) "Pemasukan" else "Pengeluaran"
            val dateStr = dateFormat.format(tx.date)

            writer.write("""<row r="$rowNum" ht="22" customHeight="1">""")
            writer.write("""<c r="A$rowNum" s="3"><v>${index + 1}</v></c>""")
            writer.write("""<c r="B$rowNum" s="3" t="inlineStr"><is><t>$dateStr</t></is></c>""")
            writer.write("""<c r="C$rowNum" s="3" t="inlineStr"><is><t>$typeStr</t></is></c>""")
            writer.write("""<c r="D$rowNum" s="2" t="inlineStr"><is><t>${escapeXml(tx.categoryName)}</t></is></c>""")
            writer.write("""<c r="E$rowNum" s="2" t="inlineStr"><is><t>${escapeXml(tx.description)}</t></is></c>""")
            writer.write("""<c r="F$rowNum" s="4"><v>${formatAmount(tx.amount)}</v></c>""")
            writer.write("""</row>""")
        }

        // Summary Rows
        val rowIncome = lastDataRow + 1
        writer.write("""<row r="$rowIncome" ht="24" customHeight="1">""")
        writer.write("""<c r="A$rowIncome" s="6"/><c r="B$rowIncome" s="6"/><c r="C$rowIncome" s="6"/><c r="D$rowIncome" s="6"/>""")
        writer.write("""<c r="E$rowIncome" s="6" t="inlineStr"><is><t>Total Pemasukan</t></is></c>""")
        writer.write("""<c r="F$rowIncome" s="8"><v>${formatAmount(totalIncome)}</v></c>""")
        writer.write("""</row>""")

        val rowExpense = lastDataRow + 2
        writer.write("""<row r="$rowExpense" ht="24" customHeight="1">""")
        writer.write("""<c r="A$rowExpense" s="6"/><c r="B$rowExpense" s="6"/><c r="C$rowExpense" s="6"/><c r="D$rowExpense" s="6"/>""")
        writer.write("""<c r="E$rowExpense" s="6" t="inlineStr"><is><t>Total Pengeluaran</t></is></c>""")
        writer.write("""<c r="F$rowExpense" s="9"><v>${formatAmount(totalExpense)}</v></c>""")
        writer.write("""</row>""")

        val rowBalance = lastDataRow + 3
        writer.write("""<row r="$rowBalance" ht="24" customHeight="1">""")
        writer.write("""<c r="A$rowBalance" s="6"/><c r="B$rowBalance" s="6"/><c r="C$rowBalance" s="6"/><c r="D$rowBalance" s="6"/>""")
        writer.write("""<c r="E$rowBalance" s="6" t="inlineStr"><is><t>Saldo Bersih</t></is></c>""")
        writer.write("""<c r="F$rowBalance" s="7"><v>${formatAmount(netBalance)}</v></c>""")
        writer.write("""</row>""")

        writer.write("""</sheetData>""")

        // OpenXML schema sequence: autoFilter must precede mergeCells
        if (totalCount > 0) {
            writer.write("""<autoFilter ref="A4:F$lastDataRow"/>""")
        }

        writer.write("""<mergeCells count="2">""")
        writer.write("""<mergeCell ref="A1:F1"/>""")
        writer.write("""<mergeCell ref="A2:F2"/>""")
        writer.write("""</mergeCells>""")

        writer.write("""</worksheet>""")
    }

    private fun formatAmount(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            amount.toLong().toString()
        } else {
            String.format(Locale.US, "%.2f", amount)
        }
    }

    private fun escapeXml(text: String?): String {
        if (text.isNullOrEmpty()) return ""
        return buildString(text.length) {
            for (char in text) {
                when (char) {
                    '&' -> append("&amp;")
                    '<' -> append("&lt;")
                    '>' -> append("&gt;")
                    '"' -> append("&quot;")
                    '\'' -> append("&apos;")
                    else -> {
                        if (char.code in 0x20..0xD7FF || char == '\t' || char == '\n' || char == '\r' || char.code in 0xE000..0xFFFD) {
                            append(char)
                        }
                    }
                }
            }
        }
    }
}
