class MonthCalendar {
    createCalendar() {
        const firstMonthDay = new Date(this.year, this.month, 1);
        const firstDay = new Date(this.year, this.month, 1);
        const firstDayOffset = firstDay.getDay();
        firstDay.setDate(firstDay.getDate() - firstDayOffset);
        if (firstDay.getDate() === 1) {
            firstDay.setDate(firstDay.getDate() - 7);
        }
        firstDay.setDate(firstDay.getDate() - 7);

        const lastDay = new Date(this.year, this.month + 1, 1);
        const lastDayOffset = 7 - firstDay.getDay();
        lastDay.setDate(lastDay.getDate() + lastDayOffset);

        let day = new Date(firstDay);
        this.container.innerHTML = '';
        const calendarContainer = document.createElement('div');
        calendarContainer.className = 'monthCalendarContainer';
        this.container.appendChild(calendarContainer);
        const title = document.createElement('div');
        title.className = 'monthCalendarCell monthCalendarHeaderTitle';
        title.innerText = firstMonthDay.toLocaleString(this.locale, { month: 'long' }) + ' ' + this.year;
        calendarContainer.appendChild(title);

        const previousMonth = document.createElement('div');
        previousMonth.className = 'monthCalendarCell monthCalendarPreviousMonthButton';
        previousMonth.innerHTML = '<';
        previousMonth.addEventListener('click', () => {
            this.selectPreviousMonth();
        });
        calendarContainer.appendChild(previousMonth);
        const nextMonth = document.createElement('div');
        nextMonth.className = 'monthCalendarCell monthCalendarNextMonthButton';
        nextMonth.innerHTML = '>';
        nextMonth.addEventListener('click', () => {
            this.selectNextMonth();
        });
        calendarContainer.appendChild(nextMonth);

        for (let i = 0; i < 10; i++) {
            if (i == 0) {
                for (let j = 0; j < 7; j++) {
                    const dayLetter = day.toLocaleDateString(this.locale, { weekday: 'narrow' })
                    const dayName = document.createElement('div');
                    dayName.className = "monthCalendarCell monthCalendarDayName"
                    dayName.innerText = dayLetter;
                    calendarContainer.appendChild(dayName);
                    day.setDate(day.getDate() + 1);
                }
            } else {
                let month = day.getMonth();
                for (let j = 0; j < 7; j++) {
                    month = day.getMonth();
                    const dayNumber = document.createElement('div');
                    dayNumber.className = "monthCalendarCell monthCalendarDayNumber"
                    if (month !== this.month) {
                        dayNumber.classList.add('monthCalendarNotThisMonth');
                    }
                    if (day.getTime() === this.today.getTime()) {
                        dayNumber.classList.add('monthCalendarToday');
                    }
                    dayNumber.innerText = day.getDate();
                    calendarContainer.appendChild(dayNumber);
                    day.setDate(day.getDate() + 1);
                }
                if (month != firstDay.getMonth() && month != this.month && day.getDay() !== 1) {
                    break;
                }
            }
        }
        Core.subscribeTo('core:setTexts', () => {
            this.locale = language;
            this.createCalendar();
        });
    };
    constructor(configuration) {
        const validateElements = () => {
            this.container = document.getElementById(configuration.id);
            if (!this.container) {
                throw new Error(`Can't find the element with the id ${configuration.id}`);
            }
        }
        const createGUI = () => {
            this.locale = navigator.language;
            this.today = new Date();
            this.today.setHours(0, 0, 0, 0);
            this.month = this.today.getMonth();
            this.year = this.today.getFullYear();
            this.createCalendar();
        }
        validateElements();
        createGUI();
    };

    selectTodayMonth() {
        this.selectedDay = new Date(this.today);
        this.createCalendar();
    };

    selectPreviousMonth() {
        this.month--;
        if (this.month < 0) {
            this.month = 11;
            this.year--;
        }
        this.createCalendar();
    };

    selectNextMonth() {
        this.month++;
        if (this.month > 11) {
            this.month = 0;
            this.year++;
        }
        this.createCalendar();
    };
}
