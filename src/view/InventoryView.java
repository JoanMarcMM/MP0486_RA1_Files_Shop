package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import main.Shop;
import model.Product;
import model.Amount;

public class InventoryView extends JDialog {
    private static final long serialVersionUID = 1L;
    private final Shop shop;
    private JTable table;
    private DefaultTableModel model;

    public InventoryView(Shop shop) {
        super();
        this.shop = shop;

        setTitle("Inventario");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Título simple
        JLabel title = new JLabel("Listado de productos", SwingConstants.LEFT);
        title.setBorder(new EmptyBorder(10, 12, 8, 12));
        add(title, BorderLayout.NORTH);

        // Modelo de tabla NO editable
        String[] cols = { "ID", "Nombre", "PVP", "Mayorista", "Disponible", "Stock" };
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0 -> Integer.class;   // ID
                    case 2, 3 -> String.class; // Amount mostrado como String
                    case 4 -> Boolean.class;   // Disponible
                    case 5 -> Integer.class;   // Stock
                    default -> String.class;   // Nombre
                };
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Footer con botón Volver
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> dispose());
        footer.add(btnVolver);
        add(footer, BorderLayout.SOUTH);

        // Cargar datos una vez
        loadData();

        pack();
        setSize(800, 400);
        setLocationRelativeTo(null);
    }

    private void loadData() {
        model.setRowCount(0);

        // Usa el que corresponda a tu clase Shop:
        List<Product> products = null;

        try {
            // Si tienes getter:
            products = shop.getInventory();
        } catch (NoSuchMethodError | UnsupportedOperationException e) {
            // Si el inventario es público:
            // products = shop.inventory;
        }

        if (products == null) return;

        for (Product p : products) {
            model.addRow(new Object[] {
                p.getId(),
                p.getName(),
                amountToString(p.getPublicPrice()),
                amountToString(p.getWholesalerPrice()),
                p.isAvailable(),
                p.getStock()
            });
        }
    }

    private String amountToString(Amount a) {
        if (a == null) return "";
        return a.toString(); // Ajusta si necesitas formato propio (moneda/decimales)
    }
}


