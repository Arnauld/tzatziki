package tzatziki.analysis.java;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Consumer;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class AvailableStepsXls {
    private final Logger log = LoggerFactory.getLogger(AvailableStepsXls.class);
    private File fileDst;
    //
    private XSSFWorkbook workbook;
    private Sheet grammarSheet;
    private int grammarSheetRowNum;
    private CellStyle headerCellStyle;
    private CellStyle entryCellStyle;

    public AvailableStepsXls(File fileDst) {
        this.fileDst = fileDst;
        ensureParentFolderExists(fileDst);

        //Blank workbook
        workbook = new XSSFWorkbook();

        //
        initStyles();

        //Create a blank sheet
        grammarSheet = workbook.createSheet("grammar");

        emitHeaderRow();
    }

    private void initStyles() {
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);

        headerCellStyle = style;
        entryCellStyle = null; // default
    }

    private void emitHeaderRow() {

        Row row = grammarSheet.createRow(grammarSheetRowNum++);

        int cellnum = 0;
        createCell(row, cellnum++, headerCellStyle, "package");
        createCell(row, cellnum++, headerCellStyle, "class");
        createCell(row, cellnum++, headerCellStyle, "method");
        createCell(row, cellnum++, headerCellStyle, "pattern");
    }

    private static void ensureParentFolderExists(File fileDst) {
        if (!fileDst.getParentFile().exists())
            fileDst.getParentFile().mkdirs();
    }

    public void emit(Grammar grammar) {
        grammar.classes().forEach(emitClassSummary(null));
        grammar.packages().forEach(emitPackageSummary());
    }

    private void emitPackageSummary(PackageEntry packageEntry) {
        log.debug("Emitting summary for package {}", packageEntry.name());

        packageEntry.subPackages().forEach(emitPackageSummary());
        packageEntry.classes().forEach(emitClassSummary(packageEntry));
    }

    private void emitClassSummary(PackageEntry packageEntry, ClassEntry classEntry) {
        log.debug("Emitting summary for class {}", classEntry.name());

        classEntry.methods().forEach(emitMethodSummary(packageEntry, classEntry));
    }

    private void emitMethodSummary(PackageEntry packageEntry, ClassEntry classEntry, MethodEntry methodEntry) {
        log.debug("Emitting summary for method {}", methodEntry.signature());

        for (String pattern : methodEntry.patterns()) {
            log.debug("Emitting pattern {}", pattern);

            Row row = grammarSheet.createRow(grammarSheetRowNum++);

            int cellnum = 0;
            createCell(row, cellnum++, entryCellStyle, classEntry.packageName());
            createCell(row, cellnum++, entryCellStyle, classEntry.name());
            createCell(row, cellnum++, entryCellStyle, methodEntry.signature());
            createCell(row, cellnum++, entryCellStyle, pattern);
        }
    }

    private void createCell(Row row, int column, CellStyle style, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        if(style != null)
            cell.setCellStyle(style);
    }

    private Consumer<? super MethodEntry> emitMethodSummary(final PackageEntry packageEntry, final ClassEntry classEntry) {
        return new Consumer<MethodEntry>() {
            @Override
            public void accept(MethodEntry methodEntry) {
                emitMethodSummary(packageEntry, classEntry, methodEntry);
            }
        };
    }

    private Consumer<? super ClassEntry> emitClassSummary(final PackageEntry packageEntry) {
        return new Consumer<ClassEntry>() {
            @Override
            public void accept(ClassEntry classEntry) {
                emitClassSummary(packageEntry, classEntry);
            }
        };
    }

    private Consumer<? super PackageEntry> emitPackageSummary() {
        return new Consumer<PackageEntry>() {
            @Override
            public void accept(PackageEntry packageEntry) {
                emitPackageSummary(packageEntry);
            }
        };
    }

    public void close() {
        try {
            FileOutputStream out = new FileOutputStream(fileDst);
            workbook.write(out);
            out.close();

            log.info("Report generated {}", fileDst.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Error while flushing report to disk", e);
        }
    }
}
