import {SET_ETHEREUM_SCAFFOLD_TEMPLATES} from "../actions/types";

const scaffoldTemplates = (state = [], action) => {
  switch (action.type) {
    case SET_ETHEREUM_SCAFFOLD_TEMPLATES:
      const newTemplates = action.payload.map(it => ({...it, currency: it.currency || 'USD'}));
      return newTemplates;
    default:
      return state;
  }
};

export default scaffoldTemplates;
