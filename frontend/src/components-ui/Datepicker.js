import React, { Component } from 'react';
import 'react-dates/initialize';
import { SingleDatePicker } from 'react-dates';
import 'react-dates/lib/css/_datepicker.css';
import styled from 'styled-components';

const DatepickerWrapper = styled.div`
  padding-top: 40px;
  text-align: center;
`;

const DatepickerLabel = styled.span`
  font-size: 14px;
  padding-right: 10px;
`;

export class Datepicker extends Component {
  state = {
    date: null,
    focused: false
  };

  componentDidMount() {
    const calendarMonth = document.querySelector('.CalendarMonth_table');
    console.log('>> ', calendarMonth);
  }

  onFocusChange = ({ focused }) => {
    const calendarMonth = document.querySelector('.CalendarMonth_table');
    console.log('>> ', calendarMonth);
    this.setState({ focused });
  };

  onDateChange = date => {
    this.setState({ date });
  };

  render() {
    return (
      <DatepickerWrapper>
        <DatepickerLabel>Expiring Date:</DatepickerLabel>{' '}
        <SingleDatePicker
          date={this.state.date}
          onDateChange={this.onDateChange}
          focused={this.state.focused}
          onFocusChange={this.onFocusChange}
          hideKeyboardShortcutsPanel
          displayFormat={'DD.MM.YYYY'}
          numberOfMonths={1}
          small
          readOnly
        />
      </DatepickerWrapper>
    );
  }
}
