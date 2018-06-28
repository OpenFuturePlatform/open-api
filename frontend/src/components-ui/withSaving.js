import React from 'react';

export const withSaving = Component =>
  class EditModal extends React.Component {
    state = {
      isSaving: false,
      transactionError: ''
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
        this.setState({ transactionError });
      }
      this.setSaving(false);
    };

    render() {
      return (
        <Component
          {...this.props}
          isSaving={this.state.isSaving}
          setSaving={this.setSaving}
          transactionError={this.state.transactionError}
          setTransactionError={this.setTransactionError}
          submitWithSaving={this.submitWithSaving}
        />
      );
    }
  };
