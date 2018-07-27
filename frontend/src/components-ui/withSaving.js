import React from 'react';

export const withSaving = Component =>
  class EditModal extends React.Component {
    state = {
      isSaving: false,
      transactionError: '',
      fieldErrors: {}
    };

    setSaving = isSaving => this.setState({ isSaving });

    setTransactionError = transactionError => this.setState({ transactionError });

    submitWithSaving = async (...args) => {
      const { onSubmit, onHide } = this.props;
      this.setSaving(true);
      this.setTransactionError('');
      try {
        await onSubmit(...args);
        onHide();
      } catch (e) {
        const transactionError = e.message;
        const fieldErrors = e.errors;
        this.setState({ fieldErrors });
        this.setState({ transactionError });
      }
      this.setSaving(false);
    };

    onShow = async () => {
      const { onShow } = this.props;
      if (typeof onShow === 'function') {
        onShow();
      }
      this.setTransactionError('');
    };

    render() {
      return (
        <Component
          {...this.props}
          onShow={this.onShow}
          isSaving={this.state.isSaving}
          setSaving={this.setSaving}
          transactionError={this.state.transactionError}
          fieldErrors={this.state.fieldErrors}
          setTransactionError={this.setTransactionError}
          submitWithSaving={this.submitWithSaving}
        />
      );
    }
  };
