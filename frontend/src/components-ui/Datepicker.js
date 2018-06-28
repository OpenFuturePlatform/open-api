import React, { Component } from 'react';
import * as moment from 'moment';
import 'react-dates/initialize';
import { SingleDatePicker } from 'react-dates';
import 'react-dates/lib/css/_datepicker.css';
import styled from 'styled-components';

const DatepickerWrapper = styled.div`
  text-align: center;
`;

const DatepickerLabel = styled.span`
  font-size: 14px;
  padding-right: 10px;
`;

export class Datepicker extends Component {
  state = {
    focused: false
  };

  render() {
    const { date, onChange } = this.props;
    const today = moment(new Date());

    return (
      <DatepickerWrapper>
        <DatepickerLabel>Expiring Date:</DatepickerLabel>{' '}
        <SingleDatePicker
          date={date}
          onDateChange={onChange}
          focused={this.state.focused}
          onFocusChange={({ focused }) => this.setState({ focused })}
          hideKeyboardShortcutsPanel
          displayFormat={'DD.MM.YYYY'}
          numberOfMonths={1}
          isOutsideRange={day => day <= today}
          small
          readOnly
        />
      </DatepickerWrapper>
    );
  }
}
