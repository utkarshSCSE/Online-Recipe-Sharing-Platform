# Online Recipe Sharing Platform

A community-driven web application to create, share, discover, and favorite recipes. Built to make it easy for home cooks and food lovers to publish recipes, follow creators, comment, and build collections.

> Note: This README is a template created for the `Online-Recipe-Sharing-Platform` repository. Adjust commands, environment variables, and examples to match the actual project structure and scripts in the repository.

## Table of contents

- [Features](#features)
- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local setup](#local-setup)
  - [Environment variables](#environment-variables)
  - [Database](#database)
  - [Running the app](#running-the-app)
- [Development workflow](#development-workflow)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- Create, read, update, and delete (CRUD) recipes
- Upload images for recipes
- Search and filter recipes by ingredients, cuisine, tags
- User registration and authentication
- Follow authors, like/favorite recipes
- Commenting and rating on recipes
- Personal collections / bookmarks
- Admin interface for moderating content (optional)

## Tech stack

This project is intended to be a full-stack web application. Typical stack (update to match this repo):

- Backend: Node.js + Express (or your preferred backend framework)
- Database: PostgreSQL (or MySQL / MongoDB)
- Authentication: JWT / Session-based
- Frontend: React / Vue / Angular (or server-rendered)
- File storage: Local uploads, AWS S3, or other object store
- Dev tooling: Docker, Docker Compose (optional), ESLint, Prettier, Jest

Replace the above with the actual stack used in this repository.

## Getting started

### Prerequisites

- Git
- Node.js (v16+ recommended)
- npm or yarn
- PostgreSQL (or DB used by the project)
- Optional: Docker & Docker Compose

### Local setup

1. Clone the repository
   ```bash
   git clone https://github.com/utkarshSCSE/Online-Recipe-Sharing-Platform.git
   cd Online-Recipe-Sharing-Platform
   ```

2. Install dependencies (adjust to package manager and locations if monorepo)
   ```bash
   # If repository contains both backend and frontend, run in each folder:
   cd backend && npm install
   cd ../frontend && npm install
   ```

### Environment variables

Create a `.env` file in the appropriate folder (e.g., `backend/`) with the required variables. Example `.env` (update keys to match code):

```
PORT=4000
NODE_ENV=development
DATABASE_URL=postgres://user:password@localhost:5432/recipes_db
JWT_SECRET=your_jwt_secret_here
AWS_S3_BUCKET=your-bucket-name
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
```

Never commit secrets to the repository. Use a secrets manager for production.

### Database

Example steps for PostgreSQL:

1. Create the database:
   ```bash
   createdb recipes_db
   ```

2. Run migrations (adjust command to your migration tool):
   ```bash
   # Example using prisma
   npx prisma migrate dev

   # Example using sequelize-cli
   npx sequelize db:migrate
   ```

3. Optionally seed sample data:
   ```bash
   npm run seed
   ```

### Running the app

Run backend and frontend (adjust commands to package scripts):

```bash
# Start backend (in backend folder)
npm run dev

# Start frontend (in frontend folder)
npm start
```

Or use Docker Compose if provided:

```bash
docker-compose up --build
```

Open http://localhost:3000 (frontend) or the port printed by your backend.

## Development workflow

- Create a new branch per feature/fix: `git checkout -b feature/add-recipe-form`
- Follow commit message guidelines (e.g., Conventional Commits)
- Open a pull request with a clear description and testing instructions
- Include tests for new features and run linters locally

## Testing

Run unit and integration tests (adjust to project scripts):

```bash
# In backend
npm test

# In frontend
npm test
```

Use CI (GitHub Actions, GitLab CI, etc.) to run tests on push and PRs.

## Deployment

High-level deployment steps:

- Build frontend for production: `npm run build`
- Deploy backend to your server / PaaS (Heroku, DigitalOcean App Platform, AWS ECS, etc.)
- Configure environment variables and secrets in production
- Use a managed database or production-ready database setup
- Configure static asset hosting (S3 + CloudFront) or serve via backend

If you use Docker, deploy via container registry and orchestration (Kubernetes, ECS, etc.).

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Run tests and linters
4. Submit a pull request describing your changes

Follow the repository's CODE_OF_CONDUCT and CONTRIBUTING guidelines if they exist.

## License

Specify repository license here (e.g., MIT). If not set, add a LICENSE file.

## Contact

- Repository: [utkarshSCSE/Online-Recipe-Sharing-Platform](https://github.com/utkarshSCSE/Online-Recipe-Sharing-Platform)
- Author / Maintainer: utkarshSCSE

---

If you'd like, I can:
- Add this README to the repository as a commit and create a PR (I will need repository write permissions),
- Or tailor the README to the exact backend/frontend stack and scripts if you tell me which technologies and npm scripts the project uses.
