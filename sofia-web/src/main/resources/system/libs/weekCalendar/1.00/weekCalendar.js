class WeekCalendar {
    createCalendar() {
        const day = this.selectedDay.getDay();
        const firstDay = new Date(this.selectedDay);
        firstDay.setHours(0, 0, 0, 0);
        const offset = firstDay.getDate() - day;
        firstDay.setDate(offset);

        const lastDay = new Date(firstDay);
        lastDay.setDate(firstDay.getDate() + 6)

        const date = new Date(firstDay);

        // const response = fetch(`/v1/calendars/data`, {
        //     method: 'GET'
        // })
        //     .then(response => response.json())
        //     .then(jsonData => {
        //         console.log(`calendar.js::createGUI::fetch response: `, jsonData);
        //     });
        // ;
        this.container.innerHTML = "";
        const calendarHeader = document.createElement('div');
        calendarHeader.className = 'calendarHeader';
        this.container.appendChild(calendarHeader);

        const calendarTitleContainer = document.createElement('div');
        calendarTitleContainer.className = 'calendarTitleContainer';
        calendarHeader.appendChild(calendarTitleContainer);

        const calendarMoveToToday = document.createElement('div');
        calendarMoveToToday.className = 'calendarToTodayButton';
        calendarMoveToToday.id = "calendarMoveToTodayButton";
        calendarMoveToToday.innerText = Core.getText('today');
        calendarTitleContainer.appendChild(calendarMoveToToday);
        calendarMoveToToday.addEventListener('click', () => {
            this.selectTodayWeek();
        });

        const calendarMoveToLastWeek = document.createElement('div');
        calendarMoveToLastWeek.className = 'calendarMoveWeekButton';
        calendarMoveToLastWeek.id = "calendarMoveToLastWeekButton";
        calendarMoveToLastWeek.innerText = "<"
        calendarTitleContainer.appendChild(calendarMoveToLastWeek);
        calendarMoveToLastWeek.addEventListener('click', () => {
            this.selectLastWeek();
        });

        const calendarMoveToNextWeek = document.createElement('div');
        calendarMoveToNextWeek.className = 'calendarMoveWeekButton';
        calendarMoveToNextWeek.id = "calendarMoveNextWeekButton";
        calendarMoveToNextWeek.innerText = ">"
        calendarTitleContainer.appendChild(calendarMoveToNextWeek);
        calendarMoveToNextWeek.addEventListener('click', () => {
            this.selectNextWeek();
        });

        const calendarTitle = document.createElement('div');
        calendarTitle.className = 'calendarTitle';
        let textForFirstMonth;
        let textForFirstYear;
        let textForLastMonth;
        let textForLastYear;

        textForLastMonth = lastDay.toLocaleString(this.locale, { month: 'long' }) + ' ';
        textForLastYear = lastDay.getFullYear();
        if (firstDay.getMonth() === lastDay.getMonth()) {
            textForFirstMonth = textForFirstYear = '';
        } else {
            textForFirstMonth = firstDay.toLocaleString(this.locale, { month: 'long' });
            if (firstDay.getFullYear() === lastDay.getFullYear()) {
                textForFirstYear = ' - ';
            } else {
                textForFirstYear = ' ' + firstDay.getFullYear() + ' - ';
            }
        }
        calendarTitle.innerText = textForFirstMonth + textForFirstYear + textForLastMonth + textForLastYear;

        calendarTitleContainer.appendChild(calendarTitle);

        const calendarParent = document.createElement('div');
        calendarParent.className = 'calendarParent';
        this.container.appendChild(calendarParent);

        for (let hour = -1; hour < 24; hour++) {
            for (let weekDay = 0; weekDay < 8; weekDay++) {
                const hourCell = document.createElement('div');
                hourCell.className = 'hourCell';
                calendarParent.appendChild(hourCell);
                if (hour === -1 && weekDay > 0) {
                    const dayOfMonth = date.getDate();
                    const name = date.toLocaleDateString(this.locale, { weekday: 'long' })
                    hourCell.innerText = `${name} ${dayOfMonth}`;
                    hourCell.classList.add('dayTitleCell');
                    if (this.today.getTime() === date.getTime()) {
                        hourCell.classList.add('calendarToday');
                    }
                    date.setDate(dayOfMonth + 1);
                }
                if (weekDay === 0 && hour !== -1) {
                    const hourLabel = document.createElement('div');
                    hourLabel.innerText = `${hour}:00`;
                    hourCell.classList.add('hourCellHour');
                    hourLabel.className = `hourLabel`;
                    hourCell.appendChild(hourLabel);
                }
            }
        }

        Core.subscribeTo('core:setTexts', () => {
            calendarMoveToToday.innerText = Core.getText('today');
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
            this.selectedDay = new Date(this.today);
            this.createCalendar();
        }
        validateElements();
        createGUI();
    };

    selectTodayWeek() {
        this.selectedDay = new Date(this.today);
        this.createCalendar();
    };

    selectLastWeek() {
        this.selectedDay.setDate(this.selectedDay.getDate() - 6)
        this.createCalendar();
    };

    selectNextWeek() {
        this.selectedDay.setDate(this.selectedDay.getDate() + 6)
        this.createCalendar();
    };
}
