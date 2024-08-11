import axios from 'axios';

const OPENAI_API_KEY = process.env.VUE_APP_OPENAI_API_KEY;
const OPENAI_BASE_URL = 'https://api.openai.com/v1/images/generations';
const CHAT_API_URL = 'https://api.openai.com/v1/chat/completions';

const translateText = async (text) => {
  const messages = [
    {
      role: 'system',
      content: 'You are a helpful assistant that translates Korean to English.'
    },
    {
      role: 'user',
      content: `Translate the following Korean text to English: "${text}"`
    }
  ];

  try {
    const response = await axios.post(
      CHAT_API_URL,
      {
        model: 'gpt-3.5-turbo',
        messages: messages,
        max_tokens: 100,
        temperature: 0.7
      },
      {
        headers: {
          'Authorization': `Bearer ${OPENAI_API_KEY}`,
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.data && response.data.choices && response.data.choices.length > 0) {
      return response.data.choices[0].message.content.trim();
    } else {
      throw new Error('Translation failed: Invalid response structure');
    }
  } catch (error) {
    console.error('Error translating text:', error);
    throw error;
  }
};

const generateImage = async (description) => {
  // 불필요한 공백 및 특수 문자를 제거하여 정리된 프롬프트 생성
  const cleanedDescription = description.replace(/[^\w\s,]/g, '').trim();
  const fullPrompt = `Portrait of a fantasy game animation character: ${cleanedDescription}. The character is emphasized with a plain background similar to a passport photo.`;
  console.log('Full Prompt:', fullPrompt);

  try {
    const response = await axios.post(
      OPENAI_BASE_URL,
      {
        prompt: fullPrompt,
        n: 1,
        size: "1024x1024"
      },
      {
        headers: {
          'Authorization': `Bearer ${OPENAI_API_KEY}`,
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.data && response.data.data && response.data.data.length > 0) {
      return response.data.data[0].url;
    } else {
      throw new Error('Image generation failed: Invalid response structure');
    }
  } catch (error) {
    console.error('Error generating image:', error.response ? error.response.data : error.message);
    throw error;
  }
};

export const handleGenerateImage = async (description) => {
  try {
    const translatedDescription = await translateText(description);
    return await generateImage(translatedDescription);
  } catch (error) {
    console.error('Error in handleGenerateImage:', error);
    throw error;
  }
};
