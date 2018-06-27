import React from 'react';

export const withSaving = Component =>
  class EditModal extends React.Component {
    state = {
      isSaving: false
    };

    setSaving = isSaving => this.setState({ isSaving });

    submitWithSaving = async () => {
      const { onSubmit, onHide } = this.props;
      this.setSaving(true);
      try {
        await onSubmit();
        onHide();
      } catch (e) {}
      this.setSaving(false);
    };

    render() {
      return (
        <Component
          {...this.props}
          isSaving={this.state.isSaving}
          setSaving={this.setSaving}
          submitWithSaving={this.submitWithSaving}
        />
      );
    }
  };
