document.addEventListener('DOMContentLoaded', function() {
  fetch('/admin/dashboard/order')
      .then(response => response.json())
      .then(data => {
          const options = {
              chart: {
                  height: "100%",
                  maxWidth: "100%",
                  type: "area",
                  fontFamily: "Inter, sans-serif",
                  dropShadow: {
                      enabled: false,
                  },
                  toolbar: {
                      show: false,
                  },
              },
              tooltip: {
                  enabled: true,
                  x: {
                      show: false,
                  },
              },
              fill: {
                  type: "gradient",
                  gradient: {
                      opacityFrom: 0.55,
                      opacityTo: 0,
                      shade: "#1C64F2",
                      gradientToColors: ["#1C64F2"],
                  },
              },
              dataLabels: {
                  enabled: false,
              },
              stroke: {
                  width: 6,
              },
              grid: {
                  show: false,
                  strokeDashArray: 4,
                  padding: {
                      left: 2,
                      right: 2,
                      top: 0
                  },
              },
              series: data.series,
              xaxis: {
                  categories: data.categories,
                  labels: {
                      show: true,
                  },
                  axisBorder: {
                      show: false,
                  },
                  axisTicks: {
                      show: false,
                  },
              },
              yaxis: {
                  show: true,
              },
          };

          if (document.getElementById("order-chart") && typeof ApexCharts !== 'undefined') {
              const chart = new ApexCharts(document.getElementById("order-chart"), options);
              chart.render();
          }
      })
      .catch(error => console.error('Error fetching chart data:', error));
});

document.addEventListener('DOMContentLoaded', function() {
    fetch('/admin/dashboard/done')
        .then(response => response.json())
        .then(data => {
            console.log('Fetched data:', data);

            const totalDeliveredEl = document.getElementById('total-delivered');
            const totalReturnedEl = document.getElementById('total-returned');
            const totalPriceEl = document.getElementById('total-price');
            const totalOrdersEl = document.getElementById('total-orders');

            if (totalDeliveredEl) totalDeliveredEl.textContent = data.totals.totalDelivered;
            if (totalReturnedEl) totalReturnedEl.textContent = data.totals.totalReturned;
            if (totalPriceEl) totalPriceEl.textContent = `${Math.round(data.totals.totalPrice)} دج`;
            if (totalOrdersEl) totalOrdersEl.textContent = data.totals.totalDelivered + data.totals.totalReturned;

            // Create bar chart for delivered/returned
            if (document.getElementById("bar-chart") && typeof ApexCharts !== 'undefined') {
                const barChartOptions = {
                    chart: {
                        type: "bar",
                        height: "100%",
                        fontFamily: "Inter, sans-serif",
                        toolbar: {
                            show: false,
                        },
                    },
                    plotOptions: {
                        bar: {
                            horizontal: false,
                            columnWidth: "55%",
                            endingShape: "rounded"
                        },
                    },
                    dataLabels: {
                        enabled: false
                    },
                    stroke: {
                        show: true,
                        width: 2,
                        colors: ["transparent"]
                    },
                    xaxis: {
                        categories: ["تسليم", "عائد"],
                        labels: {
                            show: true,
                        },
                    },
                    yaxis: {
                        title: {
                            text: "عدد الطلبيات"
                        }
                    },
                    fill: {
                        opacity: 1
                    },
                    series: [{
                        name: "الطلبيات",
                        data: [data.totals.totalDelivered, data.totals.totalReturned]
                    }],
                    colors: ["#10B981", "#EF4444"],
                };

                const barChart = new ApexCharts(document.getElementById("bar-chart"), barChartOptions);
                barChart.render();
            }
        })
        .catch(error => console.error('Error fetching chart data:', error));
});

const getChartOptions = () => {
    return {
      series: [52.8, 26.8, 20.4],
      colors: ["#1C64F2", "#16BDCA", "#9061F9"],
      chart: {
        height: 420,
        width: "100%",
        type: "pie",
      },
      stroke: {
        colors: ["white"],
        lineCap: "",
      },
      plotOptions: {
        pie: {
          labels: {
            show: true,
          },
          size: "100%",
          dataLabels: {
            offset: -25
          }
        },
      },
      labels: ["Direct", "Organic search", "Referrals"],
      dataLabels: {
        enabled: true,
        style: {
          fontFamily: "Inter, sans-serif",
        },
      },
      legend: {
        position: "bottom",
        fontFamily: "Inter, sans-serif",
      },
      yaxis: {
        labels: {
          formatter: function (value) {
            return value + "%"
          },
        },
      },
      xaxis: {
        labels: {
          formatter: function (value) {
            return value  + "%"
          },
        },
        axisTicks: {
          show: false,
        },
        axisBorder: {
          show: false,
        },
      },
    }
  }
  
  if (document.getElementById("pie-chart") && typeof ApexCharts !== 'undefined') {
    const chart = new ApexCharts(document.getElementById("pie-chart"), getChartOptions());
    chart.render();
  }

