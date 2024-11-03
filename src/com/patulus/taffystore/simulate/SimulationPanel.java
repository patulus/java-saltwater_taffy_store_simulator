package com.patulus.taffystore.simulate;

import com.patulus.taffystore.call.Call;
import com.patulus.taffystore.customer.Customer;
import com.patulus.taffystore.status.Stats;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class SimulationPanel extends JPanel {
    private Simulate simulate;
    private int currentDay;

    private JLabel totalSummaryLabel;
    private JLabel daySummaryLabel;
    private JTable customerTable;
    private JButton prevButton;
    private JButton nextButton;

    public SimulationPanel(Simulate simulate) {
        this.simulate = simulate;
        this.currentDay = 1;

        setLayout(new BorderLayout());

        // 좌측 패널 (전체 요약 정보 및 일별 요약 정보)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // 전체 요약 정보
        totalSummaryLabel = new JLabel();
        totalSummaryLabel.setVerticalAlignment(SwingConstants.TOP);
        totalSummaryLabel.setBorder(BorderFactory.createTitledBorder("전체 요약 정보"));
        updateTotalSummary();

        // 일별 요약 정보
        daySummaryLabel = new JLabel();
        daySummaryLabel.setVerticalAlignment(SwingConstants.TOP);
        daySummaryLabel.setBorder(BorderFactory.createTitledBorder("일별 요약 정보"));
        updateDaySummary();

        // 이전/다음 버튼
        JPanel buttonPanel = new JPanel();
        prevButton = new JButton("이전");
        nextButton = new JButton("다음");
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        // 이전 버튼을 누르면 현재 일 수에서 1을 뺀 일 수의 정보를 출력
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 현재 일 수가 1일일 때 이전 버튼이 동작해서는 안되므로 2일 이상일 때 버튼이 동작
                if (currentDay > 1) {
                    --currentDay;
                    updateDaySummary();
                    updateCustomerTable();
                }
            }
        });

        // 다음 버튼을 누르면 현재 일 수에서 1을 더한 일 수의 정보를 출력
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 현재 일 수가 마지막 일일 때 다음 버튼이 동작해서는 안되므로 마지막 일 수가 아닐 때 버튼이 동작
                if (currentDay < simulate.getDays()) {
                    ++currentDay;
                    updateDaySummary();
                    updateCustomerTable();
                }
            }
        });

        // 좌측 패널 구성
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BorderLayout());
        summaryPanel.add(totalSummaryLabel, BorderLayout.NORTH); // 전체 일 수에 대한 요약 정보
        summaryPanel.add(daySummaryLabel, BorderLayout.CENTER); // 현재 일 수에 대한 요약 정보

        leftPanel.add(summaryPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // 우측 패널 (개별 고객 정보 테이블)
        customerTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(customerTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("개별 고객 정보"));

        add(tableScrollPane, BorderLayout.CENTER);

        // 초기 테이블 데이터 설정
        updateCustomerTable();
    }

    // HTML을 통한 텍스트 출력
    private void updateTotalSummary() {
        String summary = simulate.getTotalSummary();
        totalSummaryLabel.setText("<html>" + summary.replaceAll("\n", "<br>") + "</html>");
    }

    private void updateDaySummary() {
        String summary = "";
        try {
            summary = simulate.getDaySummary(currentDay);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        daySummaryLabel.setText("<html>" + summary.replaceAll("\n", "<br>") + "</html>");
    }

    private void updateCustomerTable() {
        Stats stats = simulate.getStats(currentDay);

        // 열 이름 표시
        String[] columnNames = {"번호", "도착 시각", "서비스 시작 시각", "서비스 종료 시각", "서비스 시간", "대기 시간", "담당 점원", "완료 여부", "유형"};
        // 출력할 열 개수 구하기
        int totalSize = stats.getCustomerSize() + stats.getCallSize();
        // 데이터 입력을 위한 공간 생성
        Object[][] data = new Object[totalSize][columnNames.length];

        int index = 0;

        try {
            Customer customer;
            Call call;

            // 고객 정보 추가
            for (int i = 0; i < stats.getCustomerSize(); i++) {
                customer = stats.getCustomer(i);
                data[index][0] = customer.getCustNum();
                data[index][1] = customer.getArriveTime();
                data[index][2] = customer.getStartTime() >= 0 ? customer.getStartTime() : "미시작";
                data[index][3] = customer.getStartTime() + customer.getElapsedSvcTime() >= 0 ? customer.getStartTime() + customer.getElapsedSvcTime() : "미종료";
                data[index][4] = customer.getSvcTime();
                data[index][5] = customer.getStartTime() >= 0 ? customer.getStartTime() - customer.getArriveTime() : "미시작";
                data[index][6] = customer.getServer() != null ? customer.getServer().getServerNum() : "미시작";
                data[index][7] = customer.getIsDone() ? "완료" : "미완료";
                data[index][8] = customer.getCustType().getValue();
                index++;
            }

            // 전화 정보 추가
            for (int i = 0; i < stats.getCallSize(); i++) {
                call = stats.getCall(i);
                data[index][0] = call.getCustNum();
                data[index][1] = call.getArriveTime();
                data[index][2] = call.getStartTime() >= 0 ? call.getStartTime() : "미시작";
                data[index][3] = call.getStartTime() + call.getElapsedSvcTime() >= 0 ? call.getStartTime() + call.getElapsedSvcTime() : "미시작";
                data[index][4] = call.getSvcTime();
                data[index][5] = call.getStartTime() >= 0 ? call.getStartTime() - call.getArriveTime() : "미시작";
                data[index][6] = call.getServer() != null ? call.getServer().getServerNum() : "미시작";
                data[index][7] = call.getIsDone() ? "완료" : "미완료";
                data[index][8] = "CALL";
                index++;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // 테이블 내용 지정
        customerTable.setModel(new DefaultTableModel(data, columnNames));
    }


}
