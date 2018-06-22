import {SET_SCAFFOLD_TEMPLATES} from "../actions/types";

const scaffoldTemplates = (state = [], action) => {
  switch (action.type) {
    case SET_SCAFFOLD_TEMPLATES:
      const newTemplates = action.payload.map(it => ({...it, currency: it.currency || 'USD'}));
      return [...state, ...newTemplates];
    default:
      return state;
  }
};

export default scaffoldTemplates;
