package utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.StringTokenizer;

public interface Utils {
    static NumberFormat numberFormatter = NumberFormat.getNumberInstance();

    static byte gMul(byte a, byte b) {
        byte p = 0;

        for (byte counter = 0; counter < Constants.COUNT_ROWS; counter++) {
            if (((b & 0xFF) & 1) == 1) {
                p = (byte) ((p & 0xFF) ^ (a & 0xFF));
            }
            byte hi_bit_set = (byte) ((a & 0xFF) & 0x80);
            a = (byte) ((a & 0xFF) << 1);
            if (hi_bit_set != 0) {
                a = (byte) ((a & 0xFF) ^ 0x1d); /* x^8 + x^4 + x^3 + x^2 + 1 */
            }
            b = (byte) ((b & 0xFF) >> 1);
        }
        return p;
    }

    static String padding(String data, int length, String formatData)
    {
        String temp = data;


        switch (formatData) {
            case Constants.HEX:
                while (temp.length() != (length / 4))
                {
                    temp = "0" + temp;
                }
                break;
            case Constants.ASCII:
                while (temp.length() != (length / 4))
                {
                    temp = '\u00ac' + temp;
                }
                break;

        }


        return temp;
    }

    static String getStringByByteArr(byte[][] bytes, int cols) {
        StringBuilder res = new StringBuilder();

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < 8; i++) {
                String s = String.format("%x", Byte.toUnsignedInt(bytes[i][j]));
                if (s.length() < 2) {
                    s = "0" + s;
                }
                res.append(s);
            }
        }

        return res.toString();
    }

    public static void installCopyPasteHandler(TableView<?> table) {

        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());

    }


    public static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        KeyCodeCombination pasteKeyCodeCompination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_ANY);

        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {

                if( keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard( (TableView<?>) keyEvent.getSource());

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }
            else if (pasteKeyCodeCompination.match(keyEvent)) {

                if( keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    pasteFromClipboard( (TableView<?>) keyEvent.getSource());

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }

        }

    }

    /**
     * Get table selection and copy it to the clipboard.
     * @param table
     */
    public static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            // determine whether we advance in a row (tab) or a column
            // (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = "";

            Object observableValue = table.getColumns().get(col).getCellObservableValue( row);

            // null-check: provide empty string for nulls
            if (observableValue == null) {
                text = "";
            } else if( observableValue instanceof DoubleProperty) { // TODO: handle boolean etc
                text = numberFormatter.format( ((DoubleProperty) observableValue).get());
            } else if( observableValue instanceof IntegerProperty) {
                text = numberFormatter.format( ((IntegerProperty) observableValue).get());
            } else if( observableValue instanceof StringProperty) {
                text = ((StringProperty) observableValue).get();
            } else if (observableValue instanceof ObjectProperty) {
                text = (String) ((ObjectProperty) observableValue).get();
            } else {
                System.out.println("Unsupported observable value: " + observableValue);
            }

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);


    }

    public static void pasteFromClipboard( TableView<?> table) {

        // abort if there's not cell selected to start with
        if( table.getSelectionModel().getSelectedCells().size() == 0) {
            return;
        }

        // get the cell position to start with
        TablePosition pasteCellPosition = table.getSelectionModel().getSelectedCells().get(0);

        System.out.println("Pasting into cell " + pasteCellPosition);

        String pasteString = Clipboard.getSystemClipboard().getString();

        System.out.println(pasteString);

        int rowClipboard = -1;

        StringTokenizer rowTokenizer = new StringTokenizer( pasteString, "\n");
        while( rowTokenizer.hasMoreTokens()) {

            rowClipboard++;

            String rowString = rowTokenizer.nextToken();

            StringTokenizer columnTokenizer = new StringTokenizer( rowString, "\t");

            int colClipboard = -1;

            while( columnTokenizer.hasMoreTokens()) {

                colClipboard++;

                // get next cell data from clipboard
                String clipboardCellContent = columnTokenizer.nextToken();

                // calculate the position in the table cell
                int rowTable = pasteCellPosition.getRow() + rowClipboard;
                int colTable = pasteCellPosition.getColumn() + colClipboard;

                // skip if we reached the end of the table
                if( rowTable >= table.getItems().size()) {
                    continue;
                }
                if( colTable >= table.getColumns().size()) {
                    continue;
                }

                // System.out.println( rowClipboard + "/" + colClipboard + ": " + cell);

                // get cell
                TableColumn tableColumn = table.getColumns().get(colTable);
                ObservableValue observableValue = tableColumn.getCellObservableValue(rowTable);

                System.out.println( rowTable + "/" + colTable + ": " +observableValue);

                // TODO: handle boolean, etc
                if( observableValue instanceof DoubleProperty) {

                    try {

                        double value = numberFormatter.parse(clipboardCellContent).doubleValue();
                        ((DoubleProperty) observableValue).set(value);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else if( observableValue instanceof IntegerProperty) {

                    try {

                        int value = NumberFormat.getInstance().parse(clipboardCellContent).intValue();
                        ((IntegerProperty) observableValue).set(value);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else if( observableValue instanceof StringProperty) {

                    ((StringProperty) observableValue).set(clipboardCellContent);

                } else {

                    System.out.println("Unsupported observable value: " + observableValue);

                }

                System.out.println(rowTable + "/" + colTable);
            }

        }

    }
}
