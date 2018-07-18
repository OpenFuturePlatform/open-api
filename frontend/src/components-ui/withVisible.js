import React from 'react';

export const withVisible = Component =>
  class EditModal extends React.Component {
    state = {
      isVisible: false
    };

    onShow = async e => {
      if (e) {
        e.preventDefault();
      }
      this.setState({ isVisible: true });
    };

    onHide = async e => {
      if (e) {
        e.preventDefault();
      }
      this.setState({ isVisible: false });
    };

    render() {
      return <Component {...this.props} isVisible={this.state.isVisible} onShow={this.onShow} onHide={this.onHide} />;
    }
  };
