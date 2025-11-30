package ru.vsu.cs.window;

import ru.vsu.cs.additions.StreetColorSet;
import ru.vsu.cs.additions.SubwaySet;
import ru.vsu.cs.cards.*;
import ru.vsu.cs.game.Player;
import ru.vsu.cs.util.SwingUtils;
import ru.vsu.cs.additions.WindowComponentsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MonopolyWindow extends JFrame {
    private JPanel panelMain;

    private StartAndFinishCard StartAndFinishPanel;
    private JTextPane startAndFinishText;
    private StreetCard purplePanel1;
    private JTextPane purpleStreetCard1;
    private CommunityChestCard communityChestPanel1;
    private JTextPane communityChestText1;
    private StreetCard purplePanel2;
    private JTextPane purpleStreetCard2;
    private TaxCard incomeTaxPanel;
    private JTextPane incomeTaxText;
    private SubwayCard subwayPanel1;
    private JTextPane subwayText1;
    private JTextPane cyanStreetCard1;
    private StreetCard cyanPanel1;
    private ChanceCard chancePanel1;
    private JTextPane chanceText1;
    private StreetCard cyanPanel2;
    private JTextPane cyanStreetCard2;
    private StreetCard cyanPanel3;
    private JTextPane cyanStreetCard3;


    private JailCard JailPanel;
    private JTextPane jailText;
    private StreetCard beetrootPanel1;
    private JTextPane beetrootStreetCard1;
    private UtilityCard electricCompanyPanel;
    private JTextPane electricCompanyText;
    private StreetCard beetrootPanel2;
    private JTextPane beetrootStreetCard2;
    private StreetCard beetrootPanel3;
    private JTextPane beetrootStreetCard3;
    private SubwayCard subwayPanel2;
    private JTextPane subwayText2;
    private StreetCard orangePanel1;
    private JTextPane orangeStreetCard1;
    private CommunityChestCard communityChestPanel2;
    private JTextPane communityChestText2;
    private StreetCard orangePanel2;
    private JTextPane orangeStreetCard2;
    private StreetCard orangePanel3;
    private JTextPane orangeStreetCard3;


    private FreeParkingCard FreeParkingPanel;
    private JTextPane freeParkingText;
    private StreetCard redPanel1;
    private JTextPane redStreetCard1;
    private ChanceCard chancePanel2;
    private JTextPane chanceText2;
    private StreetCard redPanel2;
    private JTextPane redStreetCard2;
    private StreetCard redPanel3;
    private JTextPane redStreetCard3;
    private SubwayCard subwayPanel3;
    private JTextPane subwayText3;
    private StreetCard yellowPanel1;
    private JTextPane yellowStreetCard1;
    private StreetCard yellowPanel2;
    private JTextPane yellowStreetCard2;
    private UtilityCard waterworksPanel;
    private JTextPane waterworksText;
    private StreetCard yellowPanel3;
    private JTextPane yellowStreetCard3;


    private GoToJailCard GoToJailPanel;
    private JTextPane goToJailText;
    private StreetCard greenPanel1;
    private JTextPane greenStreetCard1;
    private StreetCard greenPanel2;
    private JTextPane greenStreetCard2;
    private CommunityChestCard communityChestPanel3;
    private JTextPane communityChestText3;
    private StreetCard greenPanel3;
    private JTextPane greenStreetCard3;
    private SubwayCard subwayPanel4;
    private JTextPane subwayText4;
    private ChanceCard chancePanel3;
    private JTextPane chanceText3;
    private StreetCard bluePanel1;
    private JTextPane blueStreetCard1;
    private TaxCard luxuryTaxPanel;
    private JTextPane luxuryTaxText;
    private StreetCard bluePanel2;
    private JTextPane blueStreetCard2;

    private MonopolyCard[][] panels;
    private JTextPane[][] panelsTextPanes;
    private Map<String, Color> monopolyColorsByDescriptions;

    private StreetCard[] streets;
    private JTextPane[] streetsTextPanes;

    private final Color purple = new Color(128, 0, 128);
    private final Color cyan = Color.CYAN;
    private final Color beetroot = new Color(127, 42, 73);
    private final Color orange = Color.ORANGE;
    private final Color red = Color.RED;
    private final Color yellow = Color.YELLOW;
    private final Color green = Color.GREEN;
    private final Color blue = Color.BLUE;

    private ArrayList<Player> allPlayers = new ArrayList<>();

    public MonopolyWindow() {
        this.setTitle("Игровой стол");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        setupMainLayout();
        this.setSize(1400, 1000);
        this.setLocationRelativeTo(null);
        panelMain.setBackground(Color.white);

        addComponentsToMainPanel();
    }

    public void setPlayers(ArrayList<Player> players) {
        this.allPlayers = new ArrayList<>(players);
        updatePlayerPositions();
    }

    // Метод для обновления позиций всех игроков
    public void updatePlayerPositions() {
        // Очищаем всех игроков со всех карточек
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                if (panels[i][j] != null) {
                    MonopolyCard card = panels[i][j];
                    for (Player player : allPlayers) {// Очищаем карточку от всех игроков
                        card.removePlayer(player);
                    }
                }
            }
        }

        // Размещаем игроков на их текущих позициях
        for (Player player : allPlayers) {
            if (!player.isBankrupt()) {
                MonopolyCard card = getPanelById(player.getPosition());
                if (card != null) {
                    card.addPlayer(player);
                }
            }
        }
        refreshAllCards();
    }

    // Метод для принудительной перерисовки всех карточек
    public void refreshAllCards() {
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                if (panels[i][j] != null) {
                    panels[i][j].repaint();
                }
            }
        }
    }

    private void setupMainLayout() {
        this.setContentPane(panelMain);
    }

    private void initializeComponents() {
        panelMain = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{0.2, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
        layout.columnWeights = new double[]{0.2, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.2};
        panelMain.setLayout(layout);

        // Добавляем центральную панель
        JPanel innerPanel = new JPanel();
        panelMain.add(innerPanel,
                new GridBagConstraints(1, 1, 11, 11, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

        // Создаем все панели
        StartAndFinishPanel = new StartAndFinishCard(0);
        startAndFinishText = new JTextPane();
        purplePanel1 = new StreetCard(1,
                "ул. Житная",
                "фиолетовый",
                60,
                new int[]{2, 10, 30, 90, 160, 250},
                50,
                50,
                30
        );
        purpleStreetCard1 = new JTextPane();
        communityChestPanel1 = new CommunityChestCard(2);
        communityChestText1 = new JTextPane();
        purplePanel2 = new StreetCard(3,
                "ул. Нагатинская",
                "фиолетовый",
                60,
                new int[]{4, 20, 60, 180, 320, 450},
                50,
                50,
                30
        );
        purpleStreetCard2 = new JTextPane();
        incomeTaxPanel = new TaxCard(4,
                "Подоходный Налог",
                200
        );
        incomeTaxText = new JTextPane();
        subwayPanel1 = new SubwayCard(5,
                "Рижская Железная Дорога"
        );
        subwayText1 = new JTextPane();
        cyanPanel1 = new StreetCard(6,
                "Варшавское Шоссе",
                "голубой",
                100,
                new int[]{6, 30, 90, 270, 400, 550},
                50,
                50,
                50
        );
        cyanStreetCard1 = new JTextPane();
        chancePanel1 = new ChanceCard(7);
        chanceText1 = new JTextPane();
        cyanPanel2 = new StreetCard(8,
                "ул. Огарёва",
                "голубой",
                100,
                new int[]{6, 30, 90, 270, 400, 550},
                50,
                50,
                50
        );
        cyanStreetCard2 = new JTextPane();
        cyanPanel3 = new StreetCard(9,
                "ул. Первая Парковая",
                "голубой",
                120,
                new int[]{8, 40, 100, 300, 450, 600},
                50,
                50,
                60
        );
        cyanStreetCard3 = new JTextPane();


        JailPanel = new JailCard(10);
        jailText = new JTextPane();
        beetrootPanel1 = new StreetCard(11,
                "ул. Полянка",
                "свекольный",
                140,
                new int[]{10, 50, 150, 450, 625, 750},
                100,
                100,
                70
        );
        beetrootStreetCard1 = new JTextPane();
        electricCompanyPanel = new UtilityCard(12,
                "Электростанция"
        );
        electricCompanyText = new JTextPane();
        beetrootPanel2 = new StreetCard(13,
                "ул. Сретенка",
                "свекольный",
                140,
                new int[]{10, 50, 150, 450, 625, 750},
                100,
                100,
                70
        );
        beetrootStreetCard2 = new JTextPane();
        beetrootPanel3 = new StreetCard(14,
                "Ростовская Набережная",
                "свекольный",
                160,
                new int[]{12, 60, 180, 500, 700, 900},
                100,
                100,
                80
        );
        beetrootStreetCard3 = new JTextPane();
        subwayPanel2 = new SubwayCard(15,
                "Курская Железная Дорога"
        );
        subwayText2 = new JTextPane();
        orangePanel1 = new StreetCard(16,
                "Рязанский Проспект",
                "оранжевый",
                180,
                new int[]{14, 70, 200, 550, 750, 950},
                100,
                100,
                90
        );
        orangeStreetCard1 = new JTextPane();
        communityChestPanel2 = new CommunityChestCard(17);
        communityChestText2 = new JTextPane();
        orangePanel2 = new StreetCard(18,
                "ул. Вавилова",
                "оранжевый",
                180,
                new int[]{14, 70, 200, 550, 750, 950},
                100,
                100,
                90
        );
        orangeStreetCard2 = new JTextPane();
        orangePanel3 = new StreetCard(19,
                "Рублёвское Шоссе",
                "оранжевый",
                200,
                new int[]{16, 80, 220, 600, 800, 1000},
                100,
                100,
                100
        );
        orangeStreetCard3 = new JTextPane();


        FreeParkingPanel = new FreeParkingCard(20);
        freeParkingText = new JTextPane();
        redPanel1 = new StreetCard(21,
                "ул. Тверская",
                "красный",
                220,
                new int[]{18, 90, 250, 700, 825, 1050},
                150,
                150,
                110
        );
        redStreetCard1 = new JTextPane();
        chancePanel2 = new ChanceCard(22);
        chanceText2 = new JTextPane();
        redPanel2 = new StreetCard(23,
                "ул. Пушкинская",
                "красный",
                220,
                new int[]{18, 90, 250, 700, 825, 1050},
                150,
                150,
                110
        );
        redStreetCard2 = new JTextPane();
        redPanel3 = new StreetCard(24,
                "Площадь Маяковского",
                "красный",
                240,
                new int[]{20, 100, 300, 750, 925, 1100},
                150,
                150,
                120
        );
        redStreetCard3 = new JTextPane();
        subwayPanel3 = new SubwayCard(25,
                "Казанская Железная Дорога"
        );
        subwayText3 = new JTextPane();
        yellowPanel1 = new StreetCard(26,
                "ул. Грузинский Вал",
                "жёлтый",
                260,
                new int[]{22, 110, 330, 800, 975, 1150},
                150,
                150,
                130
        );
        yellowStreetCard1 = new JTextPane();
        yellowPanel2 = new StreetCard(27,
                "ул. Чайковского",
                "жёлтый",
                260,
                new int[]{22, 110, 330, 800, 975, 1150},
                150,
                150,
                130
        );
        yellowStreetCard2 = new JTextPane();
        waterworksPanel = new UtilityCard(28,
                "Водопровод"
        );
        waterworksText = new JTextPane();
        yellowPanel3 = new StreetCard(29,
                "Смоленская Площадь",
                "жёлтый",
                280,
                new int[]{24, 120, 360, 850, 1025, 1200},
                150,
                150,
                140
        );
        yellowStreetCard3 = new JTextPane();


        GoToJailPanel = new GoToJailCard(30, JailPanel);
        goToJailText = new JTextPane();
        greenPanel1 = new StreetCard(31,
                "ул. Щусева",
                "зелёный",
                300,
                new int[]{26, 130, 390, 900, 1100, 1275},
                200,
                200,
                150
        );
        greenStreetCard1 = new JTextPane();
        greenPanel2 = new StreetCard(32,
                "Гоголевский Бульвар",
                "зелёный",
                300,
                new int[]{26, 130, 390, 900, 1100, 1275},
                200,
                200,
                150
        );
        greenStreetCard2 = new JTextPane();
        communityChestPanel3 = new CommunityChestCard(33);
        communityChestText3 = new JTextPane();
        greenPanel3 = new StreetCard(34,
                "Кутузовский Проспект",
                "зелёный",
                320,
                new int[]{28, 150, 450, 1000, 1200, 1400},
                200,
                200,
                160
        );
        greenStreetCard3 = new JTextPane();
        subwayPanel4 = new SubwayCard(35,
                "Ленинградская Железная Дорога"
        );
        subwayText4 = new JTextPane();
        chancePanel3 = new ChanceCard(36);
        chanceText3 = new JTextPane();
        bluePanel1 = new StreetCard(1,
                "ул. Малая Бронная",
                "синий",
                350,
                new int[]{35, 175, 500, 1100, 1300, 1500},
                200,
                200,
                175
        );
        blueStreetCard1 = new JTextPane();
        luxuryTaxPanel = new TaxCard(37,
                "Налог на Роскошь",
                100
        );
        luxuryTaxText = new JTextPane();
        bluePanel2 = new StreetCard(38,
                "ул. Арбат",
                "синий",
                400,
                new int[]{50, 200, 600, 1400, 1700, 2000},
                200,
                200,
                200
        );
        blueStreetCard2 = new JTextPane();

        electricCompanyPanel.setAnotherCard(waterworksPanel);
        waterworksPanel.setAnotherCard(electricCompanyPanel);

        chancePanel2.setDeck(chancePanel1.getDeck());
        chancePanel3.setDeck(chancePanel1.getDeck());

        communityChestPanel2.setDeck(communityChestPanel1.getDeck());
        communityChestPanel3.setDeck(communityChestPanel1.getDeck());

        StreetColorSet purpleStreets = new StreetColorSet(
                new StreetCard[]{purplePanel1, purplePanel2}
        );
        StreetColorSet cyanStreets = new StreetColorSet(
                new StreetCard[]{cyanPanel1, cyanPanel2, cyanPanel3}
        );
        StreetColorSet beetrootStreets = new StreetColorSet(
                new StreetCard[]{beetrootPanel1, beetrootPanel2, beetrootPanel3}
        );
        StreetColorSet orangeStreets = new StreetColorSet(
                new StreetCard[]{orangePanel1, orangePanel2, orangePanel3}
        );
        StreetColorSet redStreets = new StreetColorSet(
                new StreetCard[]{redPanel1, redPanel2, redPanel3}
        );
        StreetColorSet yellowStreets = new StreetColorSet(
                new StreetCard[]{yellowPanel1, yellowPanel2, yellowPanel3}
        );
        StreetColorSet greenStreets = new StreetColorSet(
                new StreetCard[]{greenPanel1, greenPanel2, greenPanel3}
        );
        StreetColorSet blueStreets = new StreetColorSet(
                new StreetCard[]{bluePanel1, bluePanel2}
        );

        for (int i = 0; i < 2; i++) {
            purpleStreets.getStreets().get(i).setColorSet(purpleStreets);
            blueStreets.getStreets().get(i).setColorSet(blueStreets);
        }

        for (int i = 0; i < 3; i++) {
            cyanStreets.getStreets().get(i).setColorSet(cyanStreets);
            beetrootStreets.getStreets().get(i).setColorSet(beetrootStreets);
            orangeStreets.getStreets().get(i).setColorSet(orangeStreets);
            redStreets.getStreets().get(i).setColorSet(redStreets);
            yellowStreets.getStreets().get(i).setColorSet(yellowStreets);
            greenStreets.getStreets().get(i).setColorSet(greenStreets);
        }

        SubwaySet subways = new SubwaySet(
                new SubwayCard[]{subwayPanel1, subwayPanel2, subwayPanel3, subwayPanel4}
        );

        for (int i = 0; i < 4; i++) {
            subways.getSubways().get(i).setSubwaySet(subways);
        }

        monopolyColorsByDescriptions = new HashMap<>();
        monopolyColorsByDescriptions.put("фиолетовый", purple);
        monopolyColorsByDescriptions.put("голубой", cyan);
        monopolyColorsByDescriptions.put("свекольный", beetroot);
        monopolyColorsByDescriptions.put("оранжевый", orange);
        monopolyColorsByDescriptions.put("красный", red);
        monopolyColorsByDescriptions.put("жёлтый", yellow);
        monopolyColorsByDescriptions.put("зелёный", green);
        monopolyColorsByDescriptions.put("синий", blue);

        StartAndFinishPanel.setTextPane(startAndFinishText);
        JailPanel.setTextPane(jailText);
        FreeParkingPanel.setTextPane(freeParkingText);
        GoToJailPanel.setTextPane(goToJailText);
    }

    private void addComponentsToMainPanel() {
        streets = new StreetCard[]
                {
                        purplePanel1, purplePanel2,
                        cyanPanel1, cyanPanel2, cyanPanel3,
                        beetrootPanel1, beetrootPanel2, beetrootPanel3,
                        orangePanel1, orangePanel2, orangePanel3,
                        redPanel1, redPanel2, redPanel3,
                        yellowPanel1, yellowPanel2, yellowPanel3,
                        greenPanel1, greenPanel2, greenPanel3,
                        bluePanel1, bluePanel2
                };

        streetsTextPanes = new JTextPane[]
                {
                        purpleStreetCard1, purpleStreetCard2,
                        cyanStreetCard1, cyanStreetCard2, cyanStreetCard3,
                        beetrootStreetCard1, beetrootStreetCard2, beetrootStreetCard3,
                        orangeStreetCard1, orangeStreetCard2, orangeStreetCard3,
                        redStreetCard1, redStreetCard2, redStreetCard3,
                        yellowStreetCard1, yellowStreetCard2, yellowStreetCard3,
                        greenStreetCard1, greenStreetCard2, greenStreetCard3,
                        blueStreetCard1, blueStreetCard2
                };

        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(purplePanel1, purpleStreetCard1, purple);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(purplePanel2, purpleStreetCard2, purple);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(cyanPanel1, cyanStreetCard1, cyan);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(cyanPanel2, cyanStreetCard2, cyan);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(cyanPanel3, cyanStreetCard3, cyan);

        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(beetrootPanel1, beetrootStreetCard1, beetroot);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(beetrootPanel2, beetrootStreetCard2, beetroot);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(beetrootPanel3, beetrootStreetCard3, beetroot);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(orangePanel1, orangeStreetCard1, orange);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(orangePanel2, orangeStreetCard2, orange);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(orangePanel3, orangeStreetCard3, orange);

        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(redPanel1, redStreetCard1, red);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(redPanel2, redStreetCard2, red);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(redPanel3, redStreetCard3, red);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(yellowPanel1, yellowStreetCard1, yellow);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(yellowPanel2, yellowStreetCard2, yellow);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(yellowPanel3, yellowStreetCard3, yellow);

        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(greenPanel1, greenStreetCard1, green);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(greenPanel2, greenStreetCard2, green);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(greenPanel3, greenStreetCard3, green);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(bluePanel1, blueStreetCard1, blue);
        WindowComponentsUtils.addTextPaneWithScrollPaneAtPanelWithColoring(bluePanel2, blueStreetCard2, blue);

        for (int i = 0; i < streets.length; i++) {
            WindowComponentsUtils.setTextAboutStreetOnHerTextPane(streets[i], streetsTextPanes[i]);
        }

        ChanceCard[] chanceCards = new ChanceCard[]
                {
                        chancePanel1, chancePanel2, chancePanel3
                };

        JTextPane[] chanceCardsTextPanes = new JTextPane[]
                {
                        chanceText1, chanceText2, chanceText3
                };

        CommunityChestCard[] communityChestCards = new CommunityChestCard[]
                {
                        communityChestPanel1, communityChestPanel2, communityChestPanel3
                };

        JTextPane[] communityChestCardsTextPanes = new JTextPane[]
                {
                        communityChestText1, communityChestText2, communityChestText3
                };

        for (int i = 0; i < 3; i++) {
            WindowComponentsUtils.addTextPaneToChanceCardAndSetTextAtHer(chanceCards[i], chanceCardsTextPanes[i]);

            WindowComponentsUtils.addTextPaneToCommunityChestCardAndSetTextAtHer(communityChestCards[i], communityChestCardsTextPanes[i]);
        }

        WindowComponentsUtils.addTextPaneToTaxCardAndSetTextAtHer(incomeTaxPanel, incomeTaxText);
        WindowComponentsUtils.addTextPaneToTaxCardAndSetTextAtHer(luxuryTaxPanel, luxuryTaxText);

        WindowComponentsUtils.addTextPaneToSubwayCardAndSetTextAtHer(subwayPanel1, subwayText1);
        WindowComponentsUtils.addTextPaneToSubwayCardAndSetTextAtHer(subwayPanel2, subwayText2);
        WindowComponentsUtils.addTextPaneToSubwayCardAndSetTextAtHer(subwayPanel3, subwayText3);
        WindowComponentsUtils.addTextPaneToSubwayCardAndSetTextAtHer(subwayPanel4, subwayText4);

        WindowComponentsUtils.addTextPaneToUtilityCardAndSetTextAtHer(electricCompanyPanel, electricCompanyText);
        WindowComponentsUtils.addTextPaneToUtilityCardAndSetTextAtHer(waterworksPanel, waterworksText);

        panels = new MonopolyCard[][]
                {
                        {
                                StartAndFinishPanel,
                                purplePanel1,
                                communityChestPanel1,
                                purplePanel2,
                                incomeTaxPanel,
                                subwayPanel1,
                                cyanPanel1,
                                chancePanel1,
                                cyanPanel2,
                                cyanPanel3,
                        },
                        {
                                JailPanel,
                                beetrootPanel1,
                                electricCompanyPanel,
                                beetrootPanel2,
                                beetrootPanel3,
                                subwayPanel2,
                                orangePanel1,
                                communityChestPanel2,
                                orangePanel2,
                                orangePanel3,
                        },
                        {
                                FreeParkingPanel,
                                redPanel1,
                                chancePanel2,
                                redPanel2,
                                redPanel3,
                                subwayPanel3,
                                yellowPanel1,
                                yellowPanel2,
                                waterworksPanel,
                                yellowPanel3,
                        },
                        {
                                GoToJailPanel,
                                greenPanel1,
                                greenPanel2,
                                communityChestPanel3,
                                greenPanel3,
                                subwayPanel4,
                                chancePanel3,
                                bluePanel1,
                                luxuryTaxPanel,
                                bluePanel2,
                        }
                };

        panelsTextPanes = new JTextPane[][]
                {
                        {
                            startAndFinishText,
                            purpleStreetCard1,
                            communityChestText1,
                            purpleStreetCard2,
                            incomeTaxText,
                            subwayText1,
                            cyanStreetCard1,
                            chanceText1,
                            cyanStreetCard2,
                            cyanStreetCard3,
                        },
                        {
                            jailText,
                            beetrootStreetCard1,
                            electricCompanyText,
                            beetrootStreetCard2,
                            beetrootStreetCard3,
                            subwayText2,
                            orangeStreetCard1,
                            communityChestText2,
                            orangeStreetCard2,
                            orangeStreetCard3,
                        },
                        {
                            freeParkingText,
                            redStreetCard1,
                            chanceText2,
                            redStreetCard2,
                            redStreetCard3,
                            subwayText3,
                            yellowStreetCard1,
                            yellowStreetCard2,
                            waterworksText,
                            yellowStreetCard3,
                        },
                        {
                            goToJailText,
                            greenStreetCard1,
                            greenStreetCard2,
                            communityChestText3,
                            greenStreetCard3,
                            subwayText4,
                            chanceText3,
                            blueStreetCard1,
                            luxuryTaxText,
                            blueStreetCard2,
                        },
                };

        // Добавление панелей по сторонам доски
        int gridX, gridY;

        for (int side = 0; side < 4; side++) {
            for (int i = 0; i < panels[side].length; i++) {
                JPanel currentPanel = panels[side][i];

                switch (side) {
                    case 0: // Левая сторона (снизу вверх)
                        gridX = 0;
                        gridY = 11 - i;
                        break;
                    case 1: // Верхняя сторона (слева направо)
                        gridX = i + 1;
                        gridY = 0;
                        break;
                    case 2: // Правая сторона (сверху вниз)
                        gridX = 12;
                        gridY = i + 1;
                        break;
                    case 3: // Нижняя сторона (справа налево)
                        gridX = 11 - i;
                        gridY = 12;
                        break;
                    default:
                        gridX = 0;
                        gridY = 0;
                }

                panelMain.add(currentPanel,
                        new GridBagConstraints(gridX, gridY, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));

                currentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
        }
    }

    public MonopolyCard getPanelById(int id) {
        MonopolyCard neededPanel = null;
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                if (panels[i][j].getCardId() == id) {
                    neededPanel = panels[i][j];
                }
            }
        }
        return neededPanel;
    }

    public int getIndexOfPanelByHer(MonopolyCard card) {
        int index = -1;
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                if (panels[i][j].getCardId() == card.getCardId()) {
                    index = i;
                }
            }
        }
        return index;
    }

    public StreetCard getStreetByStreetName(String streetName) {
        StreetCard streetCard = null;
        for (int i = 0; i < streets.length; i++) {
            if (streets[i].getStreetName().equals(streetName)) {
                streetCard = streets[i];
            }
        }
        return streetCard;
    }

    public StreetCard getStreetByIndex(int index) {
        if (index < 0 || index >= streets.length) {
            return null;
        }
        return streets[index];
    }

    public Map<String, Color> getMonopolyColorsByDescriptions() {
        return this.monopolyColorsByDescriptions;
    }

    public void winMain() throws Exception {
        Locale.setDefault(Locale.ROOT);
        SwingUtils.setDefaultFont("Microsoft Sans Serif", 18);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MonopolyWindow window = new MonopolyWindow();
        window.winMain();

        Player player = new Player(1, "игрок");
        player.setPosition(0);
        window.getPanelById(0).addPlayer(player);
    }
}